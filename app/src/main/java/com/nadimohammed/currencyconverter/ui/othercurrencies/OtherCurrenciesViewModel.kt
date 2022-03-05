package com.nadimohammed.currencyconverter.ui.othercurrencies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.domain.Result
import com.nadimohammed.domain.usecases.GetOtherCurrenciesRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class OtherCurrenciesViewModel @Inject constructor(private val getCurrencyRateUseCase: GetOtherCurrenciesRateUseCase) :
    ViewModel() {

    private val _otherCurrencyRate =
        MutableStateFlow<List<OtherCurrencyRateAdapterData>>(emptyList())
    val otherCurrencyRate: StateFlow<List<OtherCurrencyRateAdapterData>> = _otherCurrencyRate

    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    private val _onMessageError = MutableSharedFlow<Exception>()
    val onMessageError: SharedFlow<Exception> = _onMessageError

    var currencyRateToConvert: Double = 0.0

    init {
        loadOtherCurrencyRate()
    }

    private fun loadOtherCurrencyRate() {
        viewModelScope.launch {
            _apiStatus.emit(Status.LOADING)

            try {
                when (val result = getCurrencyRateUseCase.getOtherCurrencyRate()) {
                    is Result.Success -> {

                        //we are converting incoming data from api to Json Se we can easily use it and we separate the currency from the country
                        val dataToJsonString = Gson().toJson(result.results!!.rates)
                        val dataAsJsonObject = JSONObject(dataToJsonString)
                        val dataList: ArrayList<OtherCurrencyRateAdapterData> = ArrayList()

                        for (i in 0 until dataAsJsonObject.names().length()) {

                            //here we are Multiply the currency amount with other currencies
                            dataList.add(
                                OtherCurrencyRateAdapterData(
                                    dataAsJsonObject.names().getString(i),
                                    dataAsJsonObject.getDouble(
                                        dataAsJsonObject.names().getString(i)
                                    ) * currencyRateToConvert
                                )
                            )
                        }

                        _otherCurrencyRate.value = dataList

                        _apiStatus.emit(Status.SUCCESS)
                    }

                    is Result.Failed -> {
                        _apiStatus.emit(Status.ERROR)
                    }
                }

            } catch (e: Exception) {
                _onMessageError.emit(e)
                _apiStatus.emit(Status.ERROR)
                Log.e("OtherCurrenciesViewModel", e.toString())
            }

        }
    }


}