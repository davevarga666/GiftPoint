package com.davevarga.giftpoint.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) :
    ViewModel() {

    private lateinit var paymentCollection: CollectionReference

    fun getPaymentCollection(): CollectionReference {
        paymentCollection = firestore
            .collection("stripe_customers").document(auth.currentUser?.uid ?: "")
            .collection("payments")

        return paymentCollection
    }


}



