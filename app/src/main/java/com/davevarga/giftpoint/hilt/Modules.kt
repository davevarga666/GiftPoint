package com.davevarga.giftpoint.hilt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davevarga.giftpoint.BaseApplication
import com.davevarga.giftpoint.factory.DaggerViewModelFactory
import com.davevarga.giftpoint.factory.PaymentFactory
import com.davevarga.giftpoint.factory.SignInFactory
import com.davevarga.giftpoint.viewmodels.*
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Named
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TempBindModule {
    @Binds
    @Named("TempFactory")
    abstract fun bindTempViewModelFactory(factory: SignInFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TempPaymentBindModule {
    @Binds
    @Named("TempPMFactory")
    abstract fun bindTempPMViewModelFactory(factory: PaymentFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel::class)
    abstract fun bindPaymentViewModel(viewModel: PaymentViewModel): ViewModel
}


@Module
@InstallIn(SingletonComponent::class)
internal abstract class MultiBindModule {
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(SellersViewModel::class)
    abstract fun bindSellerViewModel(viewModel: SellersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderViewModel::class)
    abstract fun bindOrderViewModel(viewModel: OrderViewModel): ViewModel

}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)