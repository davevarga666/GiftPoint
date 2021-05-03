package com.davevarga.giftpoint.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.BuildConfig.STRIPE_API_KEY
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.PaymentInitFragmentBinding
import com.davevarga.giftpoint.utils.FirebaseEphemeralKeyProvider
import com.davevarga.giftpoint.viewmodels.OrderViewModel
import com.davevarga.giftpoint.viewmodels.PaymentViewModel
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentInitFragment : BaseFragment<PaymentInitFragmentBinding>() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var paymentViewModel: PaymentViewModel
    lateinit var orderViewModel: OrderViewModel

    private var flag = false
    private lateinit var paymentSession: PaymentSession
    private lateinit var selectedPaymentMethod: PaymentMethod

    private val stripe: Stripe by lazy {
        Stripe(
            requireContext(),
            PaymentConfiguration.getInstance(requireContext()).publishableKey
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flag = true
        binding.payButton.isEnabled = false

        paymentViewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel::class.java)
        orderViewModel = ViewModelProviders.of(this, factory).get(OrderViewModel::class.java)
        orderViewModel.fetchPendingOrder()

        observe()


        PaymentConfiguration.init(
            requireContext(),
            STRIPE_API_KEY
        )
        setupPaymentSession()

        setUpBindings()

    }

    private fun observe() {
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            binding.order = it

            binding.checkoutSummary.text = binding.order!!.orderValue
        })
    }

    private fun setUpBindings() {
        binding.payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        binding.paymentmethod.setOnClickListener {
            paymentSession.presentPaymentMethodSelection()
        }

        binding.toolbarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                findNavController().navigateUp()
            }
        })
    }


    private fun confirmPayment(paymentMethodId: String) {
        paymentCollection(paymentMethodId)

    }

    private fun paymentCollection(paymentMethodId: String) {
        if (flag)
            paymentViewModel.getPaymentCollection().add(
                orderViewModel.getCouponRef()
            )
                .addOnSuccessListener { documentReference ->
                    documentReference.addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            return@addSnapshotListener

                        }

                        if (snapshot != null && snapshot.exists() && flag) {
                            Log.d("payment", "Current data: ${snapshot.data}")
                            val clientSecret = snapshot.data?.get("client_secret")
                            Log.d("payment", "Create paymentIntent returns $clientSecret")
                            clientSecret?.let {
                                stripe.confirmPayment(
                                    this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                        paymentMethodId,
                                        (it as String)
                                    )
                                )
                                Log.i("PaymentInit", "payment successful")
                                binding.checkoutSummary.text = getString(R.string.thanks)
                                Toast.makeText(
                                    requireContext(),
                                    "Payment Successful!",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                orderViewModel.removeOrder()
                                findNavController().navigate(R.id.action_paymentInitFragment_to_homeScreenFragment)


                            }
                        } else {
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.i("PaymentInit", "payment failed")
                }

    }


    private fun setupPaymentSession() {
        CustomerSession.initCustomerSession(requireContext(), FirebaseEphemeralKeyProvider())
        paymentSessionDetails()
        Log.i("PaymentInit", "payment set up")
        initPaymentSession()

    }

    private fun initPaymentSession() {
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {

                    if (data.isPaymentReadyToCharge) {

                        data.paymentMethod?.let {
                            binding.paymentmethod.text =
                                "${it.card?.brand} card ends with ${it.card?.last4}"
                            binding.payButton.isEnabled = true
                            selectedPaymentMethod = it
                        }
                    }
                }

                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    Log.d("PaymentSession", "isCommunicating $isCommunicating")
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    Log.e("PaymentSession", "onError: $errorCode, $errorMessage")
                }
            }
        )
    }

    private fun paymentSessionDetails() {
        paymentSession = PaymentSession(
            this, PaymentSessionConfig.Builder()
                .setShippingInfoRequired(false)
                .setShippingMethodsRequired(false)
                .setBillingAddressFields(BillingAddressFields.None)
                .setPaymentMethodTypes(
                    listOf(PaymentMethod.Type.Card)
                )
                .build()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
    }

    override fun getFragmentView() = R.layout.payment_init_fragment

    override fun onStop() {
        flag = false
        super.onStop()
    }

}