package com.davevarga.giftpoint.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.davevarga.giftpoint.BuildConfig.STRIPE_API_KEY
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.PaymentInitFragmentBinding
import com.davevarga.giftpoint.di.DaggerAppComponent
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.utils.FirebaseEphemeralKeyProvider
import com.davevarga.giftpoint.viewmodels.PaymentViewModel
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import javax.inject.Inject

class PaymentInitFragment : BaseFragment<PaymentInitFragmentBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: PaymentViewModel

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

        DaggerAppComponent.create().inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel::class.java)

        PaymentConfiguration.init(
            requireContext(),
            STRIPE_API_KEY
        )
//        currentUser = FirebaseAuth.getInstance().currentUser
        setupPaymentSession()

        binding.payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        binding.paymentmethod.setOnClickListener {
            // Create the customer session and kick start the payment flow
            paymentSession.presentPaymentMethodSelection()
        }

    }

    private fun confirmPayment(paymentMethodId: String) {
        binding.payButton.isEnabled = false

        // Add a new document with a generated ID
        viewModel.getPaymentCollection().add(
            hashMapOf(
                "amount" to 100,
                "currency" to "usd"
            )
        )
            .addOnSuccessListener { documentReference ->
                Log.d("payment", "DocumentSnapshot added with ID: ${documentReference.id}")
                documentReference.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w("payment", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
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

                            binding.checkoutSummary.text = getString(R.string.thanks)
                            Toast.makeText(requireContext(), "Payment Done!!", Toast.LENGTH_LONG)
                                .show()
                            orderInCart = false
                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        binding.payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                binding.payButton.isEnabled = true
            }

    }

    private fun setupPaymentSession() {
        // Setup Customer Session
        CustomerSession.initCustomerSession(requireContext(), FirebaseEphemeralKeyProvider())
        // Setup a payment session
        paymentSession = PaymentSession(
            this, PaymentSessionConfig.Builder()
                .setShippingInfoRequired(false)
                .setShippingMethodsRequired(false)
                .setBillingAddressFields(BillingAddressFields.None)
                .setPaymentMethodTypes(
                    listOf(PaymentMethod.Type.Card)
                )
                .setShouldShowGooglePay(true)
                .build()
        )
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    Log.d("PaymentSession", "PaymentSession has changed: $data")
                    Log.d(
                        "PaymentSession",
                        "${data.isPaymentReadyToCharge} <> ${data.paymentMethod}"
                    )

                    if (data.isPaymentReadyToCharge) {
                        Log.d("PaymentSession", "Ready to charge");
                        binding.payButton.isEnabled = true

                        data.paymentMethod?.let {
                            Log.d("PaymentSession", "PaymentMethod $it selected")
                            binding.paymentmethod.text =
                                "${it.card?.brand} card ends with ${it.card?.last4}"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
    }

    override fun getFragmentView() = R.layout.payment_init_fragment


}