package com.davevarga.giftpoint.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.BaseApplication
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.repository.Repository
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignInViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()

    fun getUser() = repository.currentUser
    fun getMAuth() = repository.mAuth

    fun getMyGso(c: Context): GoogleSignInOptions {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(c.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return gso
    }
}