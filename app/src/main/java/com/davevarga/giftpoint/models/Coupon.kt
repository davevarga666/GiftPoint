package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Coupon(val couponValue: String) : Parcelable {
    TEN("$10"),
    TWENTY("$20"),
    FIFTY("$50"),
    HUNDRED("$100"),
//    for else branch
    ZERO("$0")


}