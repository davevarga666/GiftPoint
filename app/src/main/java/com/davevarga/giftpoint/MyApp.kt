package com.davevarga.giftpoint

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51ICrzAJUetAhy2B1Jxm7Ei1SoajWB74Qse329eJVcdm1moexF9ftaqbNhFdIQARBuAvxwlm222KQppTZ9z0r0Cob006Dzl98vK"
        )
    }
}