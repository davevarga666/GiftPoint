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

@AndroidEntryPoint
class CheckoutFragment : BaseFragment<CheckoutScreenBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: OrderViewModel
    lateinit var sellersViewModel: SellersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel::class.java)
        sellersViewModel = ViewModelProviders.of(this, factory).get(SellersViewModel::class.java)
        sellersViewModel.getSellers()
        viewModel.showPendingOrder()

        viewModel.order.observe(viewLifecycleOwner, Observer {
            binding.orderAtCheckout = it
            binding.youPay.text = "You pay " + binding.orderAtCheckout!!.orderValue
        })

        binding.editOrder.setOnClickListener {
//            viewModel.removeOrder()
//            findNavController().navigateUp()
            sellersViewModel.selectedItem = viewModel.order.value!!.seller.sellerName
            val action = CheckoutFragmentDirections.actionCheckoutFragmentToDetailFragment(sellersViewModel.sortSeller())
            findNavController().navigate(action)
        }

        binding.removeOrder.setOnClickListener {
            viewModel.removeOrder()
            findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
        }

        binding.checkoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentInitFragment)
        }


        binding.toolbarBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                findNavController().navigateUp()
            }
        })


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