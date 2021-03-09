
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
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.utils.FirebaseEphemeralKeyProvider
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

        viewModel = ViewModelProviders.of(this, factory).get(PaymentViewModel::class.java)

        PaymentConfiguration.init(
            requireContext(),
            STRIPE_API_KEY
        )
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
        paymentCollection(paymentMethodId)

    }

    private fun paymentCollection(paymentMethodId: String) {
        viewModel.getPaymentCollection().add(
            viewModel.couponRef
        )
            .addOnSuccessListener { documentReference ->
                documentReference.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val clientSecret = snapshot.data?.get("client_secret")
                        clientSecret?.let {
                            stripe.confirmPayment(
                                this, ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                    paymentMethodId,
                                    (it as String)
                                )
                            )

                            binding.checkoutSummary.text = getString(R.string.thanks)
                            orderInCart = false
                        }
                    } else {
                        binding.payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                binding.payButton.isEnabled = true
            }
    }

    private fun setupPaymentSession() {
        // Setup Customer Session
        CustomerSession.initCustomerSession(requireContext(), FirebaseEphemeralKeyProvider())
        // Setup a payment session
        paymentSessionDetails()
        initPaymentSession()

    }

    private fun initPaymentSession() {
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {

                    if (data.isPaymentReadyToCharge) {
                        binding.payButton.isEnabled = true

                        data.paymentMethod?.let {
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

    private fun paymentSessionDetails() {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paymentSession.handlePaymentData(requestCode, resultCode, data ?: Intent())
    }

    override fun getFragmentView() = R.layout.payment_init_fragment


}