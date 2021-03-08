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

//    companion object {
//        fun DocumentSnapshot.toSeller() : Seller? {
//            val name = getString("sellerName")!!
//            val email = getString("sellerEmail")!!
//            val category  = getString("productCategory")!!
//            val address = getString("businessAddress")!!
//            val account= getString("sellerAccNumber")!!
//            val image = getString("productImage")!!
//
//            return Seller(name, email, category, address, account, image)
//
//        }
//    }
}