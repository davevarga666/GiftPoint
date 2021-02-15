package com.davevarga.giftpoint.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.utils.FirebaseEphemeralKeyProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.view.BillingAddressFields
import kotlinx.android.synthetic.main.payment_init_fragment.*
import kotlinx.coroutines.delay

class PaymentInitFragment : Fragment() {

    private var currentUser: FirebaseUser? = null
    private lateinit var paymentSession: PaymentSession
    private lateinit var selectedPaymentMethod: PaymentMethod
    private val stripe: Stripe by lazy {
        Stripe(
            requireContext(),
            PaymentConfiguration.getInstance(requireContext()).publishableKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_init_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PaymentConfiguration.init(
            requireContext(),
            "pk_test_51ICrzAJUetAhy2B1Jxm7Ei1SoajWB74Qse329eJVcdm1moexF9ftaqbNhFdIQARBuAvxwlm222KQppTZ9z0r0Cob006Dzl98vK"
        )
        currentUser = FirebaseAuth.getInstance().currentUser
        setupPaymentSession()

        payButton.setOnClickListener {
            confirmPayment(selectedPaymentMethod.id!!)
        }

        paymentmethod.setOnClickListener {
            // Create the customer session and kick start the payment flow
            paymentSession.presentPaymentMethodSelection()
        }

    }

    private fun confirmPayment(paymentMethodId: String) {
        payButton.isEnabled = false

        val paymentCollection = Firebase.firestore
            .collection("stripe_customers").document(currentUser?.uid ?: "")
            .collection("payments")

        // Add a new document with a generated ID
        paymentCollection.add(
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

                            checkoutSummary.text = getString(R.string.thanks)
                            Toast.makeText(requireContext(), "Payment Done!!", Toast.LENGTH_LONG)
                                .show()
//                            Handler().postDelayed({
//                                findNavController().navigate(R.id.action_paymentInitFragment_to_homeScreenFragment)
//                            }, 3000)
                        }
                    } else {
                        Log.e("payment", "Current payment intent : null")
                        payButton.isEnabled = true
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("payment", "Error adding document", e)
                payButton.isEnabled = true
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
                        payButton.isEnabled = true

                        data.paymentMethod?.let {
                            Log.d("PaymentSession", "PaymentMethod $it selected")
                            paymentmethod.text =
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


}