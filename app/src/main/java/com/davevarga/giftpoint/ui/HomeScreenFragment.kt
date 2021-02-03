package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.HomeScreenBinding
import com.davevarga.giftpoint.models.Seller
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.home_screen.*

class HomeScreenFragment : Fragment(), SellerClickListener {

    private val db = FirebaseFirestore.getInstance()
    private val sellerRef = db.collection("sellers")
    private lateinit var sellerAdapter: SellerRecyclerAdapter
    private lateinit var binding: HomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_screen, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        searchButton.setOnClickListener { view: View ->
            findNavController().navigate(R.id.action_homeScreenFragment_to_searchFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                findNavController().navigate(R.id.action_homeScreenFragment_to_checkoutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setUpRecyclerView() {
        val query = sellerRef.orderBy("sellerName", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Seller>()
            .setQuery(query, Seller::class.java)
            .build()
        sellerAdapter = SellerRecyclerAdapter(options, this)
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = sellerAdapter
        }
    }

    override fun onItemClick(item: Seller, position: Int) {
        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToDetailFragment(item)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        sellerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        sellerAdapter.stopListening()
    }


}