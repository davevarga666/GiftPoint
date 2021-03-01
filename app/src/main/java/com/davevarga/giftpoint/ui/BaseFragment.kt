package com.davevarga.giftpoint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getFragmentView(), container, false)
        return binding.root
    }

    abstract fun getFragmentView(): Int

}

//abstract class BaseFragment<T : ViewDataBinding, VM : ViewModel> : Fragment() {
//
//    protected lateinit var binding: T
//    protected lateinit var viewModel: VM
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, getFragmentView(), container, false)
//        viewModel = ViewModelProvider(this).get(getViewModel())
//        return binding.root
//    }
//
//    abstract fun getFragmentView(): Int
//    abstract fun getViewModel(): Class<VM>
//
//}