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

class SearchFragment : Fragment(), SellerClickListener {

    val db = Firebase.firestore
    val TAG = "SearchFragment"
    private var sellerList = mutableListOf<Seller>()
    private var sellerNameList = mutableListOf<String>()
    private lateinit var selectedItem: String

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

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sellerNameList)
        businessListView.adapter = adapter


        cancel.setOnClickListener()
        {
            findNavController().navigate(R.id.action_searchFragment_to_homeScreenFragment)
        }

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
                businessListView.visibility = View.VISIBLE
                adapter.filter.filter(newText)
                return false
            }
        })


        businessListView.setOnItemClickListener { parent, view, position, id ->
            selectedItem = parent.getItemAtPosition(position) as String
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(sortSeller())
            findNavController().navigate(action)
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
    }

    private fun sortSeller(): Seller {
        var selectedSeller: Seller = Seller("", "", "", "", "")
        for (i in sellerList) {
            if (selectedItem == i.sellerName)
                selectedSeller = i

        }
        return selectedSeller
    }

}



