package com.davevarga.giftpoint.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val orderRef = firestore.collection("orders")
    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order
    val user = auth.currentUser

    fun insert(newOrder: Order) {
        viewModelScope.launch {
            orderRef.document("first")
                .set(newOrder)
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot successfully written!"
                    )
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }


    }


    fun fetchPendingOrder() {
        val docRef = orderRef.document("first")
        docRef.get()
            .addOnSuccessListener { documentSnaphot ->
                _order.value = documentSnaphot.toObject<Order>()


            }


    }

    fun removeOrder() {
        viewModelScope.launch {
            firestore.collection("orders").document("first")
                .delete()
                .addOnSuccessListener {
                    Log.d(
                        ContentValues.TAG,
                        "DocumentSnapshot successfully deleted!"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w(
                        ContentValues.TAG,
                        "Error deleting document",
                        e
                    )
                }
        }
    }

    fun isCartEmpty() = order.value == null

    fun checkFields(recipient: String, sender: String): Boolean {
        return recipient.isEmpty() && sender.isEmpty()
    }

    fun getCouponRef(): HashMap<String, Any> {
        fetchPendingOrder()
        val amountToPay = order.value!!.orderValue.replace("$", "").plus("00").toInt()
        return hashMapOf(
            "amount" to amountToPay,
            "currency" to "usd"
        )
    }


}