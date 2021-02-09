package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.DetailsScreenBinding
import com.davevarga.giftpoint.models.Coupon
import com.davevarga.giftpoint.models.Order
import com.davevarga.giftpoint.models.Recipient
import com.davevarga.giftpoint.models.Sender
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.details_screen.*


class DetailFragment : Fragment() {

    companion object {
        var orderInCart = false
    }

    private lateinit var binding: DetailsScreenBinding
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var mAuth: FirebaseAuth
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

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        namesEmpty = recipientName.toString().length == 0 && senderName.toString().length == 0

        senderName.setText(currentUser?.displayName)
        senderEmail.setText(currentUser?.email)

        binding.seller = args.sellerDetails

        Glide.with(view)
            .load(args.sellerDetails.productImage)
            .into(binding.backgroundStill)

        addToCartBtn.setOnClickListener {
            addOrder()
            findNavController().navigate(R.id.action_detailFragment_to_homeScreenFragment)

        }
        buyNowBtn.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToCheckoutFragment(addOrder())
            findNavController().navigate(action)
        }


        recipientName.addTextChangedListener(createTextWatcher())
        recipientEmail.addTextChangedListener(createTextWatcher())
        senderName.addTextChangedListener(createTextWatcher())
        senderEmail.addTextChangedListener(createTextWatcher())

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
        val buttonId = toggleGroup.checkedButtonId
        val button: MaterialButton = toggleGroup.findViewById(buttonId)
        return when (button) {
            btn10 -> Coupon.TEN.couponValue
            btn20 -> Coupon.TWENTY.couponValue
            btn50 -> Coupon.FIFTY.couponValue
            btn100 -> Coupon.HUNDRED.couponValue
            else -> Coupon.ZERO.couponValue
        }
    }

    fun addOrder(): Order {
        val seller = binding.seller
        val sender = Sender(senderName.text.toString(), senderEmail.text.toString())
        val recipient = Recipient(
            recipientName.text.toString(),
            recipientEmail.text.toString()
        )
        val coupon = couponCheck()
        orderInCart = true
        return Order(seller!!, sender, recipient, coupon)
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().isEmailValid() && !namesEmpty) {
                    addToCartBtn.setEnabled(true)
                    buyNowBtn.setEnabled(true)
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

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