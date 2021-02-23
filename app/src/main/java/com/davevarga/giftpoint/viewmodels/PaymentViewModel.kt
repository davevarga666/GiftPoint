package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PaymentViewModel() : ViewModel() {

    private val mAuth: FirebaseAuth
    val currentUser: FirebaseUser?
    val paymentCollection: CollectionReference

    init {

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        paymentCollection = Firebase.firestore
            .collection("stripe_customers").document(currentUser?.uid ?: "")
            .collection("payments")
    }


}