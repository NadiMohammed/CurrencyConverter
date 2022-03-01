package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nadimohammed.currencyconverter.databinding.CurrencyConverterFragmentBinding
import com.nadimohammed.currencyconverter.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterFragmentBinding>() {

    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModels()

    override fun getDataBinding() = CurrencyConverterFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
    }


    private fun observe() {
        lifecycleScope.launch {
            currencyConverterViewModel.currencyRate.collect {
                Log.e("TestResponse1", it.toString())
                Log.e("TestResponse2", it.rates.toString())
            }
        }

        lifecycleScope.launch {

            currencyConverterViewModel.spinnerData.collect {
                Log.e("TestResponse2", it.toString())

                val currencyCountryCode = arrayListOf<String>()
                val currencyPrice = arrayListOf<String>()

                for (i in 0 until it.size) {
                    currencyCountryCode.add(it[i].currencyCountryCode)
                    currencyPrice.add(it[i].currencyPrice)
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    currencyCountryCode
                )

                binding.fromSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedCurrencyPrice =
                                it[binding.fromSpinner.selectedItemPosition].currencyPrice
                        }
                    }

                binding.fromSpinner.adapter = adapter

            }
        }
    }

}