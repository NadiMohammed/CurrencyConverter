package com.nadimohammed.currencyconverter.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

//here are using our BaseFragment so we can initialise Data Binding so we don't need to initialize it in every activity or fragment
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getDataBinding()
        return binding.root
    }

    abstract fun getDataBinding(): VB

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

}