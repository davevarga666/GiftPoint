package com.davevarga.giftpoint.viewmodels

import androidx.lifecycle.ViewModel
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.repository.Repository
import javax.inject.Inject

class SellersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val options = repository.getOptions()
    fun getSellersList() = repository.getSellers()


    val sellerList = repository.sellerList
    val sellerNameList = repository.sellerNameList

}