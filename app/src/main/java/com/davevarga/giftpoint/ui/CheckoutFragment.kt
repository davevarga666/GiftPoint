package com.davevarga.giftpoint.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.CheckoutScreenBinding
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.ui.DetailFragment.Companion.orderInCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.checkout_screen.*

class CheckoutFragment : Fragment() {

    private lateinit var binding: CheckoutScreenBinding
    private val args: CheckoutFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private val TAG = "CheckoutFragment"

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

        binding.orderAtCheckout = args.orderToCheckout

        editOrder.setOnClickListener {
//            findNavController().navigate(R.id.action_checkoutFragment_to_detailFragment)
            findNavController().navigateUp()
        }

        removeOrder.setOnClickListener {
            orderInCart = false
            findNavController().navigate(R.id.action_checkoutFragment_to_homeScreenFragment)
        }

        checkoutButton.setOnClickListener {
            val newOrder = binding.orderAtCheckout
            db.collection("orders").document("second")
                .set(newOrder as Any)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

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