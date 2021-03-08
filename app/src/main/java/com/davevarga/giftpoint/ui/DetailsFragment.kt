package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.DetailsScreenBinding
import com.davevarga.giftpoint.models.Coupon
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.models.Recipient
import com.davevarga.giftpoint.models.Sender
import com.davevarga.giftpoint.viewmodels.OrderViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailsScreenBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: OrderViewModel

    companion object {
        var orderInCart = false
    }

    private val args: DetailFragmentArgs by navArgs()
    private var namesEmpty: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel::class.java)

        //to vm
        namesEmpty =
            binding.recipientName.toString().length == 0 && binding.senderName.toString().length == 0

        binding.senderName.setText(viewModel.getUser()?.displayName)
        binding.senderEmail.setText(viewModel.getUser()?.email)

        binding.seller = args.sellerDetails

        Glide.with(view)
            .load(args.sellerDetails.productImage)
            .into(binding.backgroundStill)

        binding.buyNowBtn.setOnClickListener {
            viewModel.insert(addOrder())

            val action = DetailFragmentDirections.actionDetailFragmentToCheckoutFragment(addOrder())
            findNavController().navigate(action)
        }


        binding.recipientName.addTextChangedListener(createTextWatcher())
        binding.recipientEmail.addTextChangedListener(createTextWatcher())
        binding.senderName.addTextChangedListener(createTextWatcher())
        binding.senderEmail.addTextChangedListener(createTextWatcher())

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


    fun couponCheck(): String {
        val buttonId = binding.toggleGroup.checkedButtonId
        val button: MaterialButton = binding.toggleGroup.findViewById(buttonId)
        return when (button) {
            binding.btn10 -> Coupon.TEN.couponValue
            binding.btn20 -> Coupon.TWENTY.couponValue
            binding.btn50 -> Coupon.FIFTY.couponValue
            binding.btn100 -> Coupon.HUNDRED.couponValue
            else -> Coupon.ZERO.couponValue
        }
    }

    fun addOrder(): Order {
        val seller = binding.seller
        val sender = Sender(binding.senderName.text.toString(), binding.senderEmail.text.toString())
        val recipient = Recipient(
            binding.recipientName.text.toString(),
            binding.recipientEmail.text.toString()
        )
        val coupon = couponCheck()
        orderInCart = true
        return Order( coupon,  recipient, seller!!, sender)
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isEmailValid() && !namesEmpty) {
                    binding.buyNowBtn.setEnabled(true)
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
//
//    override fun getViewModel() = SenderViewModel::class.java
}