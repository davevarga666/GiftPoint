package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.SearchFragmentBinding
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.viewmodels.SellersViewModel

class SearchFragment : BaseFragment<SearchFragmentBinding>(),
    SellerClickListener {

    lateinit var viewModel: SellersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(SellersViewModel::class.java)
        viewModel.getSellers()
        setupBindings(setUpAdapter())

    }

    private fun setUpAdapter(): ArrayAdapter<String> {
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                viewModel.sellerNameList
            )
        binding.businessListView.adapter = adapter
        return adapter
    }

    private fun setupBindings(adapter: ArrayAdapter<String>) {
        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cancel.setOnClickListener()
        {
            findNavController().navigate(R.id.action_searchFragment_to_homeScreenFragment)
        }

        binding.mySearchView
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    binding.mySearchView.clearFocus()
                    if (viewModel.sellerNameList.contains(query)) {
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
                viewModel.selectedItem = parent.getItemAtPosition(position) as String
                val action =
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(viewModel.sortSeller())
                findNavController().navigate(action)
            }
    }


    override fun onItemClick(item: Seller, position: Int) {}

    override fun getFragmentView() = R.layout.search_fragment

}



