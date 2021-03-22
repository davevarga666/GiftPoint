package com.davevarga.giftpoint.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davevarga.giftpoint.viewmodels.SignInViewModel
import javax.inject.Inject

class TempSignInFactory @Inject constructor(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}