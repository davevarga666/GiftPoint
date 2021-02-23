package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.HomeScreenBinding
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.viewmodels.SellersViewModel

class HomeScreenFragment : BaseFragment<HomeScreenBinding, SellersViewModel>(), SellerClickListener {

    private lateinit var sellerAdapter: SellerRecyclerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        binding.searchButton.setOnClickListener { view: View ->
            findNavController().navigate(R.id.action_homeScreenFragment_to_searchFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                if (orderInCart) {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_checkoutFragment)
                } else {
                    Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setUpRecyclerView() {

        sellerAdapter = SellerRecyclerAdapter(viewModel.options, this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sellerAdapter
        }
    }

    override fun onItemClick(item: Seller, position: Int) {
        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailFragment(item)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        sellerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        sellerAdapter.stopListening()
    }

    override fun getFragmentView() = R.layout.home_screen

    override fun getViewModel() = SellersViewModel::class.java
}