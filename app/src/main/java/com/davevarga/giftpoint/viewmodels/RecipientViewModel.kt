package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Recipient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//class RecipientViewModel : ViewModel() {
//    var recipient: Recipient
//    private val db = FirebaseFirestore.getInstance()
//    private val recipientRef = db.collection("recipient")
//
//    init {
//       recipient = fetchRecipient()
//
//    }
//
//    fun saveRecipient(sendTo: Recipient) {
//        db.collection("recipient").document("first")
//            .set(sendTo)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot written with ID: first")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }
//    }
//
//    fun fetchRecipient() {
//        viewModelScope.launch {
//            recipient = recipientRef.document("first")
//                .get()
//                .addOnSuccessListener { document ->
//                    if (document != null) {
//                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    } else {
//                        Log.d(TAG, "No such document")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "get failed with ", exception)
//                }.await().toRecipient()!!
//        }
//
//    }
//
//    fun deleteRecipient() {
//        recipientRef.document("first").delete()
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
//    }
//}