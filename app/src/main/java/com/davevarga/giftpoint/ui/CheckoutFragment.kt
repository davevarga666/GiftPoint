package com.davevarga.giftpoint.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.CheckoutScreenBinding
import com.davevarga.giftpoint.viewmodel.OrderViewModel
import com.davevarga.giftpoint.viewmodel.SellersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<CheckoutScreenBinding>() {

    private val orderViewModel: OrderViewModel by viewModels()
    private val sellersViewModel: SellersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        bindButtons()
        sellersViewModel.getSellers()
        orderViewModel.fetchPendingOrder()
        observe()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        orderViewModel.order.observe(viewLifecycleOwner, {
            binding.orderAtCheckout = it
            binding.youPay.text =
                getString(R.string.youpay) + " " +  binding.orderAtCheckout?.orderValue
        })
    }

    private fun bindButtons() {
        binding.editOrder.setOnClickListener {
            sellersViewModel.selectedItem = orderViewModel.order.value!!.seller.sellerName
            val action =
                CheckoutFragmentDirections.actionCheckoutFragmentToDetailFragment(sellersViewModel.sortSeller())
            findNavController().navigate(action)
        }

        binding.removeOrder.setOnClickListener {
            orderViewModel.removeOrder()
            findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
        }

        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentInitFragment)
        }


        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checkout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_up -> {
                findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getFragmentView() = R.layout.checkout_screen
}