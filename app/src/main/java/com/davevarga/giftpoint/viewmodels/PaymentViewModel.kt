package com.davevarga.giftpoint.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.davevarga.giftpoint.repositories.Repository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import javax.inject.Inject

class PaymentViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val repository = Repository()

    private val db = repository.db

    private lateinit var paymentCollection: CollectionReference

    fun getPaymentCollection(): CollectionReference {
        paymentCollection = db
            .collection("payments")

        return paymentCollection
    }



    }



