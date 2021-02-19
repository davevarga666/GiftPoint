package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class OrderViewModel() : ViewModel() {

    private val db = Firebase.firestore

    fun insert(newOrder: Order) {
        viewModelScope.launch {
            db.collection("orders").document("first")
                .set(newOrder as Any)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot successfully written!"
                    )
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }
    }
}