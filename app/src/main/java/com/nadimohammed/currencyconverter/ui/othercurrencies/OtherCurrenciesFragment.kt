package com.nadimohammed.currencyconverter.ui.othercurrencies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.nadimohammed.currencyconverter.databinding.OtherCurrenciesFragmentBinding
import com.nadimohammed.currencyconverter.util.BaseFragment
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.currencyconverter.util.exceptionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtherCurrenciesFragment : BaseFragment<OtherCurrenciesFragmentBinding>() {

    private val otherCurrenciesViewModel: OtherCurrenciesViewModel by viewModels()

    override fun getDataBinding() = OtherCurrenciesFragmentBinding.inflate(layoutInflater)

    // here im getting Passed data from CurrencyConverterFragment
    private val args: OtherCurrenciesFragmentArgs by navArgs()

    private lateinit var otherCurrenciesAdapter: OtherCurrenciesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()
    }

    private fun init() {
        binding.lifecycleOwner = this

        binding.viewModel = otherCurrenciesViewModel

        // here we are sending the selected currency amount by getting it from CurrencyConverterFragment to Multiply the selected currency with other currencies
        otherCurrenciesViewModel.currencyRateToConvert = args.currencyRateToConvert.toDouble()

        otherCurrenciesAdapter = OtherCurrenciesAdapter()

        binding.otherCurrenciesRecycler.adapter = otherCurrenciesAdapter

        binding.currencyCodeToConvert.text = args.currencyCodeToConvert

        binding.currencyRateToConvert.text = args.currencyRateToConvert.toString()

    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    otherCurrenciesViewModel.apiStatus.collect {
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
                    otherCurrenciesViewModel.onMessageError.collect {
                        exceptionHandler(requireContext(), it)
                    }
                }

            }
        }
    }

}