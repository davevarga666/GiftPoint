package com.davevarga.giftpoint

import com.davevarga.giftpoint.di.DaggerAppComponent
import com.stripe.android.PaymentConfiguration
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

//declare later in Manifest as base class
//class BaseApplication : DaggerApplication() {
//
//    override fun onCreate() {
//        super.onCreate()
//        PaymentConfiguration.init(
//            applicationContext,
//            BuildConfig.STRIPE_API_KEY
//        )
//    }
//
//    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//        return DaggerAppComponent.builder().application(this).build()
//    }
//}