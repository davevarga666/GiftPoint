package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.models.Seller
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.seller_name_list_item.*

class SearchFragment : Fragment(), SellerClickListener {

    val db = Firebase.firestore
    val TAG = "SearchFragment"
    private var sellerList = mutableListOf<Seller>()
    private var sellerNameList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        do this in vm
        getSellers()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sellerNameList)
        businessListView.adapter = adapter
        mySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mySearchView.clearFocus()
                if (sellerNameList.contains(query)) {
                    adapter.filter.filter(query)
                } else {
                    Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

//        businessListView.onItemClickListener {
//            sellerNameItem.liste {
////                clickListener.onItemClick(seller, adapterPosition)
//            }
//        }

        cancel.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeScreenFragment)
        }


    }

    fun getSellers() {
        db.collection("sellers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    sellerList.add(document.toObject(Seller::class.java))
                    sellerNameList.add(document.toObject(Seller::class.java).sellerName)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    override fun onItemClick(item: Seller, position: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(item)
        findNavController().navigate(action)
    }
}