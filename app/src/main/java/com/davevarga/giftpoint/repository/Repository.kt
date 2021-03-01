package com.davevarga.giftpoint.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.models.Seller
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


class Repository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()
    private val sellerRef = db.collection("sellers")
    val sellerList = mutableListOf<Seller>()
    val sellerNameList = mutableListOf<String>()
    private lateinit var options: FirestoreRecyclerOptions<Seller>
    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    private lateinit var paymentCollection: CollectionReference

    fun getOptions(): FirestoreRecyclerOptions<Seller> {
        val query = sellerRef.orderBy("sellerName", Query.Direction.ASCENDING)
        options = FirestoreRecyclerOptions.Builder<Seller>()
            .setQuery(query, Seller::class.java)
            .build()
        return options
    }

    fun getSellers() {
        getOptions()
        db.collection("sellers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    sellerList.add(document.toObject(Seller::class.java))
                    sellerNameList.add(document.toObject(Seller::class.java).sellerName)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    fun insert(newOrder: Order) {

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

    fun getPaymentCollection(): CollectionReference {
        paymentCollection = Firebase.firestore
            .collection("stripe_customers").document(currentUser?.uid ?: "")
            .collection("payments")

        return paymentCollection
    }
}