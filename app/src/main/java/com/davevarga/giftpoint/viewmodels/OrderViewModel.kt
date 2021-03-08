package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.repository.Repository
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val db = repository.db
    private val orderRef = db.collection("orders")
    fun getUser() = repository.currentUser
    var order: Order? = Order()


    fun insert(newOrder: Order) {

        viewModelScope.launch {
            orderRef.document("first")
                .set(newOrder as Order)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot successfully written!"
                    )
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }


    }

    fun showPendingOrder() {
        val docRef = orderRef.document("first")
        docRef.get()
            .addOnSuccessListener { documentSnaphot ->
                order = documentSnaphot.toObject<Order>()

            }
    }

    fun removeOrder() {
        db.collection("orders").document("first")
            .delete()
            .addOnSuccessListener {
                Log.d(
                    ContentValues.TAG,
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
    }


}