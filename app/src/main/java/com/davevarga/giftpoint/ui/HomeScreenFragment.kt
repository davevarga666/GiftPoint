package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.HomeScreenBinding
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.viewmodels.SellersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<HomeScreenBinding>(), SellerClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: SellersViewModel

    private lateinit var sellerAdapter: SellerRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(SellersViewModel::class.java)
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

        sellerAdapter = SellerRecyclerAdapter(viewModel.getOptions(), this)
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