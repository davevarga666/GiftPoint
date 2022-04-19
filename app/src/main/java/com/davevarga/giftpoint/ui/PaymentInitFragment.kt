package com.davevarga.giftpoint.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.BuildConfig.STRIPE_API_KEY
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.PaymentInitFragmentBinding
import com.davevarga.giftpoint.util.FirebaseEphemeralKeyProvider
import com.davevarga.giftpoint.viewmodel.OrderViewModel
import com.davevarga.giftpoint.viewmodel.PaymentViewModel
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentInitFragment : BaseFragment<PaymentInitFragmentBinding>() {

    private val paymentViewModel: PaymentViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
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
        binding.payButton.isVisible = false
        binding.payButton.isEnabled = true
        orderViewModel.fetchPendingOrder()
        observe()

        PaymentConfiguration.init(
            requireContext(),
            STRIPE_API_KEY
        )
        setUpPaymentSession()
        setUpBindings()

    }

    private fun observe() {
        orderViewModel.order.observe(viewLifecycleOwner, {
            binding.order = it
            binding.checkoutSummary.text = binding.order?.orderValue
        })
    }

    private fun setUpBindings() {
        binding.payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        binding.paymentMethod.setOnClickListener {
            paymentSession.presentPaymentMethodSelection()
        }

        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun confirmPayment(paymentMethodId: String) {
        initPaymentCollection(paymentMethodId)
    }

    private fun initPaymentCollection(paymentMethodId: String) {
        flag = true
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
                        }
                    }
                }
                .addOnFailureListener {
                    Log.i("PaymentInit", "payment failed")
                }

    }


    private fun setUpPaymentSession() {
        CustomerSession.initCustomerSession(requireContext(), FirebaseEphemeralKeyProvider())
        setPaymentSessionDetails()
        Log.i("PaymentInit", "payment set up")
        initPaymentSession()
    }

    private fun initPaymentSession() {
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                @SuppressLint("SetTextI18n")
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {

                    if (data.isPaymentReadyToCharge) {

                        data.paymentMethod?.let {
                            binding.paymentMethod.text =
                                "${it.card?.brand} card ends with ${it.card?.last4}"
                            binding.payButton.isVisible = true
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

    private fun setPaymentSessionDetails() {
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