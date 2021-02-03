package com.davevarga.giftpoint.ui

import android.os.Bundle
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
import kotlinx.android.synthetic.main.details_screen.*

class DetailFragment : Fragment() {

    private lateinit var binding: DetailsScreenBinding
    private val args: DetailFragmentArgs by navArgs()

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

        binding.seller = args.sellerDetails

        Glide.with(view)
            .load(args.sellerDetails.productImage)
            .into(binding.backgroundStill)

        addToCartBtn.setOnClickListener {
            //save the object somewhere, load it in Checkout
            addOrder()
            findNavController().navigate(R.id.action_detailFragment_to_homeScreenFragment)

        }
        buyNowBtn.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToCheckoutFragment(addOrder())
            findNavController().navigate(action)
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

    fun couponCheck(): String {
        val buttonId = toggleGroup.checkedButtonId
        val button: MaterialButton = toggleGroup.findViewById(buttonId)
//        button.isChecked = true
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
        return Order(seller!!, sender, recipient, coupon)
    }
}