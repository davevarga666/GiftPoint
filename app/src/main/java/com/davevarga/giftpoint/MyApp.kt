package com.davevarga.giftpoint

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davevarga.giftpoint.BuildConfig.STRIPE_API_KEY
import com.davevarga.giftpoint.factory.DaggerViewModelFactory
import com.davevarga.giftpoint.ui.*
import com.davevarga.giftpoint.viewmodels.*
import com.stripe.android.PaymentConfiguration
import dagger.Binds
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            STRIPE_API_KEY
        )
    }
}