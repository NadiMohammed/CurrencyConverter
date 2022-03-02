package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
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

    private var selectedCurrencyPriceFromSpinner: Double = 0.0
    private var selectedCurrencyPriceToSpinner: Double = 0.0
    private var selectedItemPositionFromSpinner =0
    private var selectedItemPositionToSpinner =0
    private var stopAmountEditTextChangedListener: Boolean = false
    private var swipeValueFromTo: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        onClick()

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
                val currencyPrice = arrayListOf<Double>()

                for (i in 0 until it.size) {
                    currencyCountryCode.add(it[i].currencyCountryCode)
                    currencyPrice.add(it[i].currencyPrice)
                }

                val spinnerAdapter = ArrayAdapter(
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
//                            val selectedCurrencyPriceFromSpinner = it[binding.fromSpinner.selectedItemPosition].currencyPrice
                            selectedCurrencyPriceFromSpinner =
                                it[binding.fromSpinner.selectedItemPosition].currencyPrice


                        }
                    }

                binding.toSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
//                            val selectedCurrencyPriceToSpinner = it[binding.toSpinner.selectedItemPosition].currencyPrice
                            selectedCurrencyPriceToSpinner =
                                it[binding.toSpinner.selectedItemPosition].currencyPrice

                         }
                    }

                binding.fromSpinner.adapter = spinnerAdapter
                binding.toSpinner.adapter = spinnerAdapter

            }
        }
    }

    private fun onClick() {

        binding.amountEdt.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        stopAmountEditTextChangedListener = false
                        Log.e(
                            "stopAmountEditTextChangedListener1",
                            stopAmountEditTextChangedListener.toString()
                        )
                    } //Do Something
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding.convertedValueEdt.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        stopAmountEditTextChangedListener = true
                        Log.e(
                            "stopAmountEditTextChangedListener2",
                            stopAmountEditTextChangedListener.toString()
                        )

                    }//Do Something
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding.amountEdt.doOnTextChanged { text, start, before, count ->

            if (stopAmountEditTextChangedListener == false) { //No Keep it Work

                if (!binding.amountEdt.text.isNullOrEmpty()) {
                    binding.convertedValueEdt.setText(
                        (binding.amountEdt.text.toString()
                            .toDouble() * selectedCurrencyPriceToSpinner).toString()
                    )
                }

            } else {
                return@doOnTextChanged
            }
        }

        binding.convertedValueEdt.doOnTextChanged { text, start, before, count ->
            if (stopAmountEditTextChangedListener == true) { // Yes StopAmountEditTextChangedListener

                if (!binding.convertedValueEdt.text.isNullOrEmpty()) {
                    binding.amountEdt.setText(
                        (binding.convertedValueEdt.text.toString()
                            .toDouble() * selectedCurrencyPriceFromSpinner).toString()
                    )
                }

            } else {
                return@doOnTextChanged
            }
        }

        binding.switchImg.setOnClickListener {
            selectedItemPositionFromSpinner = binding.fromSpinner.selectedItemPosition
            selectedItemPositionToSpinner = binding.toSpinner.selectedItemPosition
            binding.fromSpinner.setSelection(selectedItemPositionToSpinner)
            binding.toSpinner.setSelection(selectedItemPositionFromSpinner)
            binding.amountEdt.text  = binding.convertedValueEdt.text

            Log.e("getselectedItem1", binding.fromSpinner.selectedItem.toString())
            Log.e("getselectedItem2", selectedCurrencyPriceFromSpinner.toString())
            Log.e("getselectedItem3", selectedCurrencyPriceToSpinner.toString())
        }

    }

}