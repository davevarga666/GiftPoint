package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.repositories.Repository
import javax.inject.Inject

class SenderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getUser() = repository.currentUser

}