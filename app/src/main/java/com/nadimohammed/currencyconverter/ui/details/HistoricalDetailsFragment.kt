package com.nadimohammed.currencyconverter.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nadimohammed.currencyconverter.databinding.HistoricalDetailsFragmentBinding
import com.nadimohammed.currencyconverter.util.BaseFragment
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.currencyconverter.util.exceptionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoricalDetailsFragment : BaseFragment<HistoricalDetailsFragmentBinding>() {

    private val historicalDetailsViewModel: HistoricalDetailsViewModel by viewModels()

    override fun getDataBinding() = HistoricalDetailsFragmentBinding.inflate(layoutInflater)

    private lateinit var historicalDetailsAdapter: HistoricalDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()
    }

    /*
   * I Use This Function To initialize all things will be used when starting
   * like DataBinding
   * binding.lifecycleOwner used for observing LiveData with data binding
   * binding.viewModel = currencyConverterViewModel is used here to set value of viewModel variable in HistoricalDetailsFragment.xml
   * */
    private fun init() {
        binding.lifecycleOwner = this

        binding.viewModel = historicalDetailsViewModel

        historicalDetailsAdapter = HistoricalDetailsAdapter()

        binding.historicalDetailsRecycler.adapter = historicalDetailsAdapter
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
                    historicalDetailsViewModel.apiStatus.collect {
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
                    historicalDetailsViewModel.onMessageError.collect {
                        exceptionHandler(requireContext(), it)
                    }
                }

            }
        }
    }


}