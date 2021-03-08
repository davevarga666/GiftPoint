package com.davevarga.giftpoint.utils

import android.util.Log
import androidx.annotation.Size
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import java.io.IOException

class FirebaseEphemeralKeyProvider: EphemeralKeyProvider {

    override fun createEphemeralKey(
        apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener
    ) {
        val data = hashMapOf(
            "api_version" to apiVersion
        )

        Firebase.functions
            .getHttpsCallable("createEphemeralKey")
            .call(data)
            .continueWith { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val message = e.message
                        Log.e("EphemeralKey", "Ephemeral key provider returns error: $e $code $message")
                    }
                }
                val key = task.result?.data.toString()
                Log.d("EphemeralKey", "Ephemeral key provider returns $key")
                keyUpdateListener.onKeyUpdate(key)
            }
    }

}