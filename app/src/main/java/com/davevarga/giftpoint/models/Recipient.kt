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

}