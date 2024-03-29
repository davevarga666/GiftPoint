package com.davevarga.giftpoint

import android.app.Application
import com.google.firebase.FirebaseApp
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            BuildConfig.STRIPE_API_KEY
        )
    }
}