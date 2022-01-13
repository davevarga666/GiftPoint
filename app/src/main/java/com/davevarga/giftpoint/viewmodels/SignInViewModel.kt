package com.davevarga.giftpoint.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.repositories.Repository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getMAuth() = repository.mAuth
    fun getUser() = repository.currentUser

    fun getMyGso(c: Context): GoogleSignInOptions {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(c.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return gso
    }
}