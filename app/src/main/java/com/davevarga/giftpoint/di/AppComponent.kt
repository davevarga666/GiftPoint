//package com.davevarga.giftpoint.di
//
//import android.app.Application
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.davevarga.giftpoint.factory.DaggerViewModelFactory
//import com.davevarga.giftpoint.ui.*
//import com.davevarga.giftpoint.viewmodels.*
//import dagger.*
//import dagger.multibindings.IntoMap
//import javax.inject.Singleton
//import kotlin.reflect.KClass
//
//@Singleton
//@Component(modules = [MultiBindModule::class, AppModule::class])
//interface AppComponent {
//    fun inject(homeFragment: HomeScreenFragment)
//    fun inject(signInFragment: SignInFragment)
//    fun inject(searchFragment: SearchFragment)
//    fun inject(detailFragment: DetailFragment)
//    fun inject(checkoutFragment: CheckoutFragment)
//    fun inject(paymentFragment: PaymentInitFragment)
//}
//
//@Module
//internal abstract class MultiBindModule {
//    @Binds
//    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SignInViewModel::class)
//    abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SellersViewModel::class)
//    abstract fun bindSellerViewModel(viewModel: SellersViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SenderViewModel::class)
//    abstract fun bindSenderViewModel(viewModel: SenderViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(OrderViewModel::class)
//    abstract fun bindOrderViewModel(viewModel: OrderViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(PaymentViewModel::class)
//    abstract fun bindPaymentViewModel(viewModel: PaymentViewModel): ViewModel
//}
//
//@MustBeDocumented
//@Target(AnnotationTarget.FUNCTION)
//@Retention(AnnotationRetention.RUNTIME)
//@MapKey
//internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
//
//@Module
//class AppModule(private val application: Application) {
//
//    @Provides
//    @Singleton
//    fun providesApplication(): Application = application
//
//    @Provides
//    @Singleton
//    fun providesApplicationContext(): Context = application
//
//}