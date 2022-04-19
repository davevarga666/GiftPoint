package com.davevarga.giftpoint.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    fun getMAuth() = auth
    fun getUser() = auth.currentUser

    fun getMyGso(c: Context): GoogleSignInOptions {

        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(c.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}