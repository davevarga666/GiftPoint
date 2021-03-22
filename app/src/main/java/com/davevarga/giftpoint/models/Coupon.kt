package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Coupon(val couponValue: String) : Parcelable  {
    TEN("$1000"),
    TWENTY("$2000"),
    FIFTY("$5000"),
    HUNDRED("$10000"),
//    for else branch
    ZERO("$0")


}