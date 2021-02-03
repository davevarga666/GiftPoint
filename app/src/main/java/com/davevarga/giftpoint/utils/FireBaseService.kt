package com.davevarga.giftpoint.utils

import com.davevarga.giftpoint.models.Seller
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

//object FireBaseService {
//    private const val TAG = "FirebaseService"
//
//    suspend fun getSellerData(sellerId: String): Seller? {
//        val db = FirebaseFirestore.getInstance()
//        return db.collection("sellers").document(sellerId).get().await()
//    }
//}