package com.nadimohammed.currencyconverter.ui.currencyconverter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nadimohammed.currencyconverter.R

class CurrencyConverterFragment : Fragment() {

    private lateinit var viewModel: CurrencyConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.currency_converter_fragment, container, false)
    }


}