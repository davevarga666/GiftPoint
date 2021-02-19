package com.davevarga.giftpoint.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sender(
    var senderName: String,
    var senderEmail: String

) : Parcelable {
}