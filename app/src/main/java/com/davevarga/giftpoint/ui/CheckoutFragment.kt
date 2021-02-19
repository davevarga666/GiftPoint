package com.davevarga.giftpoint.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.CheckoutScreenBinding
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.davevarga.giftpoint.viewmodels.OrderViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckoutFragment : Fragment() {

    private lateinit var binding: CheckoutScreenBinding
    private lateinit var orderViewModel: OrderViewModel
    private val args: CheckoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.checkout_screen, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        binding.orderAtCheckout = args.orderToCheckout

        binding.editOrder.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutFragment_to_detailFragment)
        }

        binding.removeOrder.setOnClickListener {
            orderInCart = false
            findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
        }

        binding.checkoutButton.setOnClickListener {
            orderViewModel.insert(binding.orderAtCheckout!!)

            orderInCart = false
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentInitFragment)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checkout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_up -> {
                findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}