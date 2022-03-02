package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(private val getCurrencyRateUseCase: GetCurrencyRateUseCase) :
    ViewModel() {

    private val _currencyRate = MutableSharedFlow<CurrencyRateEntitie>()
    val currencyRate: SharedFlow<CurrencyRateEntitie> = _currencyRate

    private val _spinnerData = MutableStateFlow(ArrayList<SpinnerData>())
    val spinnerData: StateFlow<ArrayList<SpinnerData>> = _spinnerData

    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    init {
        loadCurrencyRate()
    }

    private fun loadCurrencyRate() {
        viewModelScope.launch {
            _apiStatus.emit(Status.LOADING)

            try {
                when (val result = getCurrencyRateUseCase.getCurrencyRate()) {
                    is Result.Success -> {
                        _currencyRate.emit(result.results!!)

                        val ratesJsonString = Gson().toJson(result.results!!.rates)
                        val json = JSONObject(ratesJsonString)
                        val spinnerData: ArrayList<SpinnerData> = ArrayList()

                        for (i in 0 until json.names().length()) {

                            spinnerData.add(
                                SpinnerData(
                                    (json.names().getString(i)),
//                                    json.getString(json.names().getString(i))
                                    json.getDouble(json.names().getString(i))
                                )
                            )
                        }

                        _spinnerData.value = spinnerData
                        _apiStatus.emit(Status.SUCCESS)
                    }

                    is Result.Failed -> {
                        _apiStatus.emit(Status.ERROR)
                    }
                }

            } catch (e: Exception) {
                _apiStatus.emit(Status.ERROR)
                Log.e("CurrencyConverterViewModel", e.toString())
            }

        }
    }

}