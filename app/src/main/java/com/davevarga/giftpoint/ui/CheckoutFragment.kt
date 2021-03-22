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
import androidx.navigation.fragment.navArgs
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.CheckoutScreenBinding
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.viewmodels.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<CheckoutScreenBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: OrderViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel::class.java)
        viewModel.showPendingOrder()

        viewModel.order.observe(viewLifecycleOwner, Observer {
            binding.orderAtCheckout = it
            binding.youPay.text = "You pay " + binding.orderAtCheckout!!.orderValue.dropLast(2)
        })


        binding.editOrder.setOnClickListener {
            //handle nav in different use cases
            viewModel.removeOrder()
            findNavController().navigate(R.id.action_checkoutFragment_to_detailFragment)
        }

        binding.removeOrder.setOnClickListener {
            orderInCart = false
            viewModel.removeOrder()
            findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
        }

        binding.checkoutButton.setOnClickListener {
            orderInCart = false
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentInitFragment)
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