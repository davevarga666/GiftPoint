package com.davevarga.giftpoint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipient(
    var recipientName: String = "",
    var recipientEmail: String = ""
) : Parcelable