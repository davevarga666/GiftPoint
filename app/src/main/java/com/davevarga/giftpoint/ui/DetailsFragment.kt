package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.davevarga.giftpoint.viewmodels.SenderViewModel
import com.google.android.material.button.MaterialButton

class DetailFragment : Fragment() {

    companion object {
        var orderInCart = false
    }

    private lateinit var senderViewModel: SenderViewModel
    private lateinit var binding: DetailsScreenBinding
    private val args: DetailFragmentArgs by navArgs()
    private var namesEmpty: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.details_screen, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        senderViewModel = ViewModelProviders.of(this).get(SenderViewModel::class.java)
        namesEmpty =
            binding.recipientName.toString().length == 0 && binding.senderName.toString().length == 0

        binding.senderName.setText(senderViewModel.currentUser?.displayName)
        binding.senderEmail.setText(senderViewModel.currentUser?.email)

        binding.seller = args.sellerDetails

        Glide.with(view)
            .load(args.sellerDetails.productImage)
            .into(binding.backgroundStill)

        binding.buyNowBtn.setOnClickListener {
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
        return Order(seller!!, sender, recipient, coupon)
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
}