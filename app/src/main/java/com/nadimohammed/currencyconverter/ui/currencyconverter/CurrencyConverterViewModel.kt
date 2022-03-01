package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.usecases.GetCurrencyRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val getCurrencyRateUseCase: GetCurrencyRateUseCase) :
    ViewModel() {

    private val _currencyRate = MutableSharedFlow<CurrencyRateEntitie>()
    val currencyRate: SharedFlow<CurrencyRateEntitie> = _currencyRate

    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    init {
        loadCurrencyRate()
    }

    fun loadCurrencyRate() {
        viewModelScope.launch {
            _apiStatus.emit(Status.LOADING)

            try {
                when (val result = getCurrencyRateUseCase.getCurrencyRate()) {
                    is Result.Success -> {
                        currencyRate.emit(result.results!!)
                        _apiStatus.emit(Status.SUCCESS)

                    }
                    is Result.Failed -> {
                        _apiStatus.emit(Status.ERROR)
                    }
                }

            } catch (e: Exception) {
                Log.e("CurrencyConverterViewModel", e.toString())
            }

        }
    }

}