package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.repositories.Repository
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val db = repository.db
    private lateinit var paymentCollection: CollectionReference

    fun getPaymentCollection(): CollectionReference {
        paymentCollection = db
            .collection("stripe_customers").document(repository.currentUser?.uid ?: "")
            .collection("payments")

        return paymentCollection
    }


}



