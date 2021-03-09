package com.davevarga.giftpoint.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.repository.Repository
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import javax.inject.Inject

class SellersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val db = repository.db
    private val sellerRef = db.collection("sellers")
    lateinit var selectedItem: String
    var selectedSeller = MutableLiveData<Seller>()
    private lateinit var options: FirestoreRecyclerOptions<Seller>

    val sellerList = mutableListOf<Seller>()
    val sellerNameList = mutableListOf<String>()

    fun sortSeller(): Seller {
        var selectedSeller = Seller("", "", "", "", "", "")
        for (i in sellerList) {
            if (selectedItem == i.sellerName)
                selectedSeller = i

        }
        return selectedSeller
    }

    fun getSellers() {

        viewModelScope.launch {
            getOptions()
            sellerRef
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                        sellerList.add(document.toObject(Seller::class.java))
                        sellerNameList.add(document.toObject(Seller::class.java).sellerName)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }

    }

    fun getOptions(): FirestoreRecyclerOptions<Seller> {
        val query = sellerRef.orderBy("sellerName", Query.Direction.ASCENDING)
        options = FirestoreRecyclerOptions.Builder<Seller>()
            .setQuery(query, Seller::class.java)
            .build()
        return options
    }

}