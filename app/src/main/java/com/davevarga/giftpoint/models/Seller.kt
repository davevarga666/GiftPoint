package com.davevarga.giftpoint.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seller(
    var sellerName: String = "",
    var sellerEmail: String = "",
    var productImage: String = "",
    var productCategory: String = "",
    var businessAddress: String = "",
    var sellerAccNumber: String = ""


) : Parcelable {

}