package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.HomeScreenBinding
import com.davevarga.giftpoint.databinding.SearchFragmentBinding
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.viewmodels.SellersViewModel

class SearchFragment : Fragment(), SellerClickListener {

    private lateinit var selectedItem: String
    private lateinit var sellersViewModel: SellersViewModel
    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_fragment, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellersViewModel = ViewModelProviders.of(this).get(SellersViewModel::class.java)
        sellersViewModel.getSellers()

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sellersViewModel.sellerNameList)
        binding.businessListView.adapter = adapter


        binding.cancel.setOnClickListener()
        {
            findNavController().navigate(R.id.action_searchFragment_to_homeScreenFragment)
        }

        binding.mySearchView
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    binding.mySearchView.clearFocus()
                    if (sellersViewModel.sellerNameList.contains(query)) {
                        adapter.filter.filter(query)
                    } else {
                        Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_LONG).show()
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    binding.businessListView.visibility = View.VISIBLE
                    adapter.filter.filter(newText)
                    return false
                }
            })


        binding.businessListView
            .setOnItemClickListener { parent, view, position, id ->
                selectedItem = parent.getItemAtPosition(position) as String
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(sortSeller())
                findNavController().navigate(action)
            }

    }


    override fun onItemClick(item: Seller, position: Int) {
    }

    private fun sortSeller(): Seller {
        var selectedSeller = Seller("", "", "", "", "")
        for (i in sellersViewModel.sellerList) {
            if (selectedItem == i.sellerName)
                selectedSeller = i

        }
        return selectedSeller
    }

}



