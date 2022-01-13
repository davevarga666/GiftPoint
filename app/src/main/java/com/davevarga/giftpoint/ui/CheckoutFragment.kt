package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.CheckoutScreenBinding
import com.davevarga.giftpoint.viewmodels.OrderViewModel
import com.davevarga.giftpoint.viewmodels.SellersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class CheckoutFragment : BaseFragment<CheckoutScreenBinding>() {

    lateinit var orderViewModel: OrderViewModel
    lateinit var sellersViewModel: SellersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        bindButtons()
        orderViewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
        sellersViewModel = ViewModelProvider(requireActivity()).get(SellersViewModel::class.java)
        sellersViewModel.getSellers()
        orderViewModel.fetchPendingOrder()
        observe()


    }

    private fun observe() {
        orderViewModel.order.observe(viewLifecycleOwner, Observer {
            binding.orderAtCheckout = it
            binding.youPay.text = "You pay " + binding.orderAtCheckout?.orderValue ?: "20"
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