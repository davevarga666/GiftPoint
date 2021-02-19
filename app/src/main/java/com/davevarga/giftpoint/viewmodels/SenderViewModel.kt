package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SenderViewModel : ViewModel() {
    private val mAuth: FirebaseAuth
    val currentUser: FirebaseUser?

    init {
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser

    }
}