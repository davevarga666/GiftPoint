package com.davevarga.giftpoint.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
//    var orderId: Int,
    var seller: Seller,
    var sender: Sender,
    var recipient: Recipient,
    var orderValue: String
) : Parcelable {

}