package com.davevarga.giftpoint.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipient(
//    var recipientId: Int,
    var recipientName: String,
    var recipientEmail: String
) : Parcelable {

//    companion object {
//        fun DocumentSnapshot.toRecipient() : Recipient? {
//            val name = getString("recipientName")!!
//            val email = getString("recipientEmail")!!
//
//            return Recipient(name, email)
//
//        }
//    }

}