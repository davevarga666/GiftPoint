package com.davevarga.giftpoint.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.repositories.Repository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject

class SignInViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()

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