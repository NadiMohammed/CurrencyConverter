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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoricalDetailsFragment : BaseFragment<HistoricalDetailsFragmentBinding>() {

    private val historicalDetailsViewModel: HistoricalDetailsViewModel by viewModels()

    private lateinit var historicalDetailsAdapter: HistoricalDetailsAdapter

    override fun getDataBinding() = HistoricalDetailsFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observe()

    }

    private fun init() {
        historicalDetailsAdapter = HistoricalDetailsAdapter()

        binding.historicalDetailsRecycler.adapter = historicalDetailsAdapter
    }

    private fun observe() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                historicalDetailsViewModel.apiStatus.collect {
                    when (it) {
                        Status.SUCCESS -> {
                            historicalDetailsViewModel.historicalDetails.collect {
                                historicalDetailsAdapter.submitList(it)
                            }
                        }
                    }
                }
            }
        }

    }


}