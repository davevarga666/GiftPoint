package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
//    var orderId: Int,
    var seller: Seller = Seller("", "", "", "", "", ""),
    var sender: Sender = Sender("", ""),
    var recipient: Recipient = Recipient("", ""),
    var orderValue: String = ""
) : Parcelable {

}