package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
//    var orderId: Int,
    var orderValue: String = "",
    var recipient: Recipient = Recipient("", ""),
    var seller: Seller = Seller("", "", "", "", "", ""),
    var sender: Sender = Sender("", "")
) : Parcelable {

}