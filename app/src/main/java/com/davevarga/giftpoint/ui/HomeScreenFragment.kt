package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.adapter.SellerClickListener
import com.davevarga.giftpoint.adapter.SellerRecyclerAdapter
import com.davevarga.giftpoint.databinding.HomeScreenBinding
import com.davevarga.giftpoint.model.Seller
import com.davevarga.giftpoint.viewmodel.OrderViewModel
import com.davevarga.giftpoint.viewmodel.SellersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<HomeScreenBinding>(), SellerClickListener {

    private val sellersViewModel: SellersViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var sellerAdapter: SellerRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setRecyclerView()
        setBindings()
        orderViewModel.fetchPendingOrder()
    }

    private fun setBindings() {
        binding.searchButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_searchFragment)
        }

        binding.cart.setOnClickListener {
            if (orderViewModel.isCartEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_homeScreenFragment_to_checkoutFragment)
            }
        }

        binding.cart.setOnClickListener {
            if (orderViewModel.isCartEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_homeScreenFragment_to_checkoutFragment)
            }
        }

    }

    private fun setRecyclerView() {
        sellerAdapter = SellerRecyclerAdapter(sellersViewModel.getOptions(), this)
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

}