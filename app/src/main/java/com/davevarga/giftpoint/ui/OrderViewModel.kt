package com.davevarga.giftpoint.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.utils.FirebaseService
import kotlinx.coroutines.launch

class OrderViewModel(newOrder: Order) : ViewModel() {
    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order

//    init {
//        viewModelScope.launch {
//            insert(newOrder)
//            _order = FirebaseService.getOrder()
//        }
//    }

    fun insert(freshOrder: Order) {
        viewModelScope.launch {
            FirebaseService.insertOrder(freshOrder)
        }
    }
}