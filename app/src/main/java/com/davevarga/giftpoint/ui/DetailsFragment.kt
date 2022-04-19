package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.DetailsScreenBinding
import com.davevarga.giftpoint.model.Coupon
import com.davevarga.giftpoint.model.Order
import com.davevarga.giftpoint.model.Recipient
import com.davevarga.giftpoint.model.Sender
import com.davevarga.giftpoint.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailsScreenBinding>() {

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var couponValue: String
    private val args by navArgs<DetailFragmentArgs>()
    private var namesEmpty: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.fetchPendingOrder()
        couponValue = Coupon.TEN.couponValue
        namesEmpty = viewModel.checkFields(binding.recipientName.toString(), binding.senderName.toString())
        setBindings()
        setUpImageLoading(view)
    }

    private fun setUpImageLoading(view: View) {
        Glide.with(view)
            .load(args.sellerDetails?.productImage)
            .into(binding.backgroundStill)
    }

    private fun setBindings() {
        binding.buyNowBtn.isEnabled = false
        binding.senderName.setText(viewModel.user?.displayName)
        binding.senderEmail.setText(viewModel.user?.email)
        binding.recipientName.setText(viewModel.order.value?.recipient?.recipientName ?: "")
        binding.recipientEmail.setText(viewModel.order.value?.recipient?.recipientEmail ?: "")
        binding.seller = args.sellerDetails

        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.recipientName.addTextChangedListener(createTextWatcher())
        binding.recipientEmail.addTextChangedListener(createTextWatcher())
        binding.senderName.addTextChangedListener(createTextWatcher())
        binding.senderEmail.addTextChangedListener(createTextWatcher())

        binding.buyNowBtn.setOnClickListener {
            viewModel.checkFields(binding.recipientName.toString(), binding.senderName.toString())
            viewModel.insert(addOrder())

            val action = DetailFragmentDirections.actionDetailFragmentToCheckoutFragment(addOrder())
            findNavController().navigate(action)
        }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                couponValue = when (checkedId) {
                    R.id.btn10 -> Coupon.TEN.couponValue
                    R.id.btn20 -> Coupon.TWENTY.couponValue
                    R.id.btn50 -> Coupon.FIFTY.couponValue
                    R.id.btn100 -> Coupon.HUNDRED.couponValue
                    else -> Coupon.ZERO.couponValue
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_up -> {
                findNavController().navigate(R.id.action_detailFragment_to_homeScreenFragment)
                true
            }
            R.id.action_cart -> {
                findNavController().navigate(R.id.action_detailFragment_to_checkoutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addOrder(): Order {
        val seller = binding.seller
        val sender = Sender(binding.senderName.text.toString(), binding.senderEmail.text.toString())
        val recipient = Recipient(
            binding.recipientName.text.toString(),
            binding.recipientEmail.text.toString()
        )
        return Order(couponValue, recipient, seller!!, sender)
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isEmailValid() && !namesEmpty) {
                    binding.buyNowBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        }
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    override fun getFragmentView() = R.layout.details_screen
}