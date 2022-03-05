package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nadimohammed.currencyconverter.R
import com.nadimohammed.currencyconverter.databinding.CurrencyConverterFragmentBinding
import com.nadimohammed.currencyconverter.util.BaseFragment
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.currencyconverter.util.exceptionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment<CurrencyConverterFragmentBinding>() {

    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModels()

    override fun getDataBinding() = CurrencyConverterFragmentBinding.inflate(layoutInflater)

    private var selectedItemPositionFromSpinner = 0
    private var selectedItemPositionToSpinner = 0
    private var firstSwitch: Boolean = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()
        onClick()
    }

    /*
      * I Use This Function To initialize all things will be used when starting
      * like DataBinding
      * binding.lifecycleOwner used for observing LiveData with data binding
      * binding.viewModel = currencyConverterViewModel is used here to set value of viewModel variable in currency_converter_fragment.xml
      * */
    private fun init() {
        binding.lifecycleOwner = this

        binding.viewModel = currencyConverterViewModel
    }

    /*
    * I Use This Function To validate Edittext Input To Avoid Crash
    * */
    private fun isValid(): Boolean {
        if (binding.fromEdt.text!!.isNullOrEmpty()) {
            binding.fromEdt.error = "يجب كتابه رقم"
            binding.fromEdt.requestFocus()
            return false
        }

        if (binding.toEdt.text!!.isNullOrEmpty()) {
            binding.toEdt.error = "يجب كتابه رقم"
            binding.toEdt.requestFocus()
            return false
        }

        return true
    }

    /*
         * I Use This Function To observe variable from ViewModel and update our views when something got change
         * like when apiStatus got change it update progressBar visibility
         * and onMessageError when it change it show us the error message
         * viewLifecycleOwner.lifecycleScope.launch is used to execute the code inside at coroutines
         * viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) is to code execute when Lifecycle STARTED state and cancels when the Lifecycle is STOPPED
         * */
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    currencyConverterViewModel.apiStatus.collect {
                        when (it) {
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                            }
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                }

                launch {
                    currencyConverterViewModel.onMessageError.collect {
                        exceptionHandler(requireContext(), it)
                    }
                }

            }
        }
    }

    /*
   * I Use This Function to handle all button and EditText Click
   * */
    private fun onClick() {

        binding.detailsBtn.setOnClickListener {
            navigateToHistoricalDetails()
        }

        binding.otherCurrenciesBtn.setOnClickListener {
            navigateToOtherCurrencies()
        }

        val fromTextWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValid()) {
                    currencyConverterViewModel.handleToEditTextData(
                        binding.fromEdt.text.toString().toDouble()
                    )
                }
            }
        }

        val toTextWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (isValid()) {
                    currencyConverterViewModel.handleFromEditTextData(
                        binding.toEdt.text.toString().toDouble()
                    )
                }
            }
        }

        binding.fromEdt.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.fromEdt.addTextChangedListener(fromTextWatcher)
            } else {
                binding.fromEdt.removeTextChangedListener(fromTextWatcher)
            }
        }

        binding.toEdt.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.toEdt.addTextChangedListener(toTextWatcher)
            } else {
                binding.toEdt.removeTextChangedListener(toTextWatcher)
            }
        }

        binding.switchImg.setOnClickListener {

            if (firstSwitch == false && isValid()) {
                currencyConverterViewModel.switchToFrom.value = false
                currencyConverterViewModel.switchFromTo.value = true

                selectedItemPositionToSpinner = binding.toSpinner.selectedItemPosition
                selectedItemPositionFromSpinner = binding.fromSpinner.selectedItemPosition

                binding.fromSpinner.setSelection(selectedItemPositionToSpinner) //selectedItemPositionToSpinner
                binding.toSpinner.setSelection(selectedItemPositionFromSpinner) //selectedItemPositionFromSpinner

                currencyConverterViewModel.handleSwitchButtonClick(
                    binding.fromEdt.text.toString().toDouble()
                )
                firstSwitch = true
                return@setOnClickListener

            }

            if (firstSwitch == true && isValid()) {
                currencyConverterViewModel.switchToFrom.value = true
                currencyConverterViewModel.switchFromTo.value = false

                selectedItemPositionToSpinner = binding.toSpinner.selectedItemPosition
                selectedItemPositionFromSpinner = binding.fromSpinner.selectedItemPosition

                binding.fromSpinner.setSelection(selectedItemPositionToSpinner) //selectedItemPositionToSpinner
                binding.toSpinner.setSelection(selectedItemPositionFromSpinner) //selectedItemPositionFromSpinner

                currencyConverterViewModel.handleSwitchButtonClick(
                    binding.fromEdt.text.toString().toDouble()
                )
                firstSwitch = false
                return@setOnClickListener
            }

        }
    }

    /*
* I Use This Function to navigate from currencyConverterFragment to historicalDetailsFragment
* */
    private fun navigateToHistoricalDetails() {
        requireView().findNavController()
            .navigate(R.id.action_currencyConverterFragment_to_historicalDetailsFragment)
    }

    //here we are passing data(selected country code and currency amount) to OtherCurrenciesFragment
    private fun navigateToOtherCurrencies() {
        Navigation.findNavController(requireView()).navigate(
            CurrencyConverterFragmentDirections.actionCurrencyConverterFragmentToOtherCurrenciesFragment(
                binding.fromSpinner.selectedItem.toString(),
                binding.fromEdt.text.toString().toFloat()
            )
        )
    }

}