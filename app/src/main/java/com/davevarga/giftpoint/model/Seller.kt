package com.davevarga.giftpoint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seller(
    var sellerName: String = "",
    var sellerEmail: String = "",
    var productImage: String = "",
    var productCategory: String = "",
    var businessAddress: String = "",
    var sellerAccNumber: String = ""

) : Parcelable