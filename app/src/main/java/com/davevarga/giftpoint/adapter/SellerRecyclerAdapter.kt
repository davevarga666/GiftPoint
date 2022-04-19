package com.davevarga.giftpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.SellerListItemBinding
import com.davevarga.giftpoint.model.Seller
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SellerRecyclerAdapter(
    options: FirestoreRecyclerOptions<Seller>,
    private val clickListener: SellerClickListener
) :
    FirestoreRecyclerAdapter<Seller, SellerRecyclerAdapter.SellerViewHolder>(options) {

    private lateinit var binding: SellerListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.seller_list_item,
            parent, false
        )

        return SellerViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SellerViewHolder, position: Int, model: Seller) {
        return holder.bind(model, binding, clickListener)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class SellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            seller: Seller,
            binding: SellerListItemBinding,
            clickListener: SellerClickListener
        ) {

            binding.seller = seller

            Glide.with(itemView.context)
                .load(seller.productImage)
                .override(400, 400)
                .into(binding.couponImage)

            itemView.setOnClickListener {
                clickListener.onItemClick(seller, bindingAdapterPosition)
            }
        }
    }

}

interface SellerClickListener {
    fun onItemClick(item: Seller, position: Int)
}