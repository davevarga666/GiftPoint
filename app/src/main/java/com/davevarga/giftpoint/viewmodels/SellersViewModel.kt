package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.models.Seller
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SellersViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val sellerRef = db.collection("sellers")
    var sellerList = mutableListOf<Seller>()
    var sellerNameList = mutableListOf<String>()
    val options: FirestoreRecyclerOptions<Seller>


    init {
        val query = sellerRef.orderBy("sellerName", Query.Direction.ASCENDING)
        options = FirestoreRecyclerOptions.Builder<Seller>()
            .setQuery(query, Seller::class.java)
            .build()
    }


    fun getSellers() {
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


}