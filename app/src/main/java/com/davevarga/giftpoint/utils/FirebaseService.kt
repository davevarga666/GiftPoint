package com.davevarga.giftpoint.utils

import android.util.Log
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.models.Seller
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object FirebaseService {
    private const val TAG = "FirebaseService"

    //for HomeScreen
    suspend fun getSellerData(sellerId: String): FirestoreRecyclerOptions<Seller> {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("sellers").orderBy("sellerName", Query.Direction.ASCENDING)

        return FirestoreRecyclerOptions.Builder<Seller>()
            .setQuery(query, Seller::class.java)
            .build()
    }


    suspend fun insertOrder(order: Order) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders").document("first")
            .set(order as Any)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}