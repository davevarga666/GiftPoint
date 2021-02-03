package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sender(
    var senderName: String,
    var senderEmail: String

) : Parcelable {
}