package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.repositories.Repository
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val db = repository.db
    private val orderRef = db.collection("orders")
    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order
    val user = repository.currentUser

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


    fun fetchPendingOrder() {
        val docRef = orderRef.document("first")
        docRef.get()
            .addOnSuccessListener { documentSnaphot ->
                _order.value = documentSnaphot.toObject<Order>()


            }


    }

    fun removeOrder() {
        viewModelScope.launch {
            db.collection("orders").document("first")
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

    fun getCouponRef(): HashMap<String, out Any> {
        fetchPendingOrder()
        val amountToPay = order.value!!.orderValue.replace("$", "").plus("00").toInt()
        return hashMapOf(
            "amount" to amountToPay,
            "currency" to "usd"
        )
    }


}