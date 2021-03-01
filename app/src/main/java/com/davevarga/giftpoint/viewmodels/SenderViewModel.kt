package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SenderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getUser() = repository.currentUser

}