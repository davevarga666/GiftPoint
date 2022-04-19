package com.davevarga.giftpoint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var orderValue: String = "",
    var recipient: Recipient = Recipient("", ""),
    var seller: Seller = Seller("", "", "", "", "", ""),
    var sender: Sender = Sender("", "")
) : Parcelable