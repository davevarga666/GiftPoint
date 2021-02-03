package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipient(
//    var recipientId: Int,
    var recipientName: String,
    var recipientEmail: String
) : Parcelable {
}