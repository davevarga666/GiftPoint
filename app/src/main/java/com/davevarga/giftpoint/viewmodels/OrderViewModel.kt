package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.repository.Repository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel@Inject constructor(private val repository: Repository) : ViewModel() {

    fun getUser() = repository.currentUser
    fun insert(newOrder: Order) {
        viewModelScope.launch {
            repository.insert(newOrder)
        }
    }
}