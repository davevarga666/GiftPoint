package com.davevarga.giftpoint.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class PaymentViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val repository = Repository()

    private val db = repository.db
    val couponRef = hashMapOf(
        "amount" to 100,
        "currency" to "usd"
    )

    private lateinit var paymentCollection: CollectionReference


    fun getPaymentCollection(): CollectionReference {
        paymentCollection = db
            .collection("stripe_customers").document(repository.currentUser?.uid ?: "")
            .collection("payments")

        return paymentCollection
    }

    }



