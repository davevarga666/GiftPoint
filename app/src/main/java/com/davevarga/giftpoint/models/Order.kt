package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
//    var orderId: Int,
    var seller: Seller,
    var sender: Sender,
    var recipient: Recipient,
    var orderValue: String
) : Parcelable {
}