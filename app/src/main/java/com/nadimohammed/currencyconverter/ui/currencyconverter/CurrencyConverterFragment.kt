package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nadimohammed.currencyconverter.databinding.CurrencyConverterFragmentBinding
import com.nadimohammed.currencyconverter.util.BaseFragment
import com.nadimohammed.currencyconverter.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterFragmentBinding>() {

    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModels()

    override fun getDataBinding() = CurrencyConverterFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        loadFromSpinnerCurrency()
    }


    private fun observe() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                currencyConverterViewModel.apiStatus.collect {
                    when (it) {
                        Status.SUCCESS -> {
                            Log.e(
                                "TestResponse",
                                currencyConverterViewModel.currencyRate.toString()
                            )
                        }

                        else -> {
                            Log.e("TestResponseError", "Error")
                        }
                    }
                }

            }
        }
    }

    private fun loadFromSpinnerCurrency() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                val currencyRate = arrayListOf<String>()

                currencyConverterViewModel.currencyRate.collect {
                    for (i in 0 until it.rates.size) {
                        currencyRate.add(it.rates[i].toString())
                        Log.e("LoopTest", it.rates[i].toString())
                    }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        currencyRate
                    )


                    binding.fromSpinner.adapter = adapter

                    binding.fromSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val currencyCode =
                                    it.rates[binding.fromSpinner.selectedItemPosition]
                                Log.e("currencyCode", currencyCode.toString())

                            }
                        }
                }
            }
        }
    }


}