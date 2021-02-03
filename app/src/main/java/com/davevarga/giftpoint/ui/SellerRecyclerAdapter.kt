package com.davevarga.giftpoint.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.SellerListItemBinding
import com.davevarga.giftpoint.models.Seller
import com.davevarga.giftpoint.utils.GlideApp
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.FirebaseStorage

class SellerRecyclerAdapter(options: FirestoreRecyclerOptions<Seller>, var clickListener: SellerClickListener) :
    FirestoreRecyclerAdapter<Seller, SellerRecyclerAdapter.SellerViewHolder>(options) {

    lateinit var binding: SellerListItemBinding

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
        return holder.bind(model, binding, clickListener!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class SellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(seller: Seller, binding: SellerListItemBinding, clickListener: SellerClickListener) {

            itemView.setOnClickListener {
                clickListener.onItemClick(seller, adapterPosition)
            }

            binding.seller = seller
            Glide.with(itemView.context)
                .load(seller.productImage)
                .into(binding.couponImage)


        }
    }

}

interface SellerClickListener {
    fun onItemClick(item: Seller, position: Int)
}