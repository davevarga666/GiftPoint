package com.davevarga.giftpoint.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Seller
import kotlinx.coroutines.launch

class SellerViewModel : ViewModel() {
    private val _seller = MutableLiveData<Seller>()
    val seller: LiveData<Seller> = _seller
}