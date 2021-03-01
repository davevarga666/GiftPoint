package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class PaymentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getPaymentCollection() = repository.getPaymentCollection()


}