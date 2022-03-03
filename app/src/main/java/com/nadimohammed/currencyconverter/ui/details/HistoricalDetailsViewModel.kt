package com.nadimohammed.currencyconverter.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.data.db.DatabaseCurrency
import com.nadimohammed.data.repository.CurrencyLocalDataSourceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalDetailsViewModel @Inject constructor(private val currencyLocalDataSourceImpl: CurrencyLocalDataSourceImpl) :
    ViewModel() {

    private val _historicalDetails = MutableSharedFlow<List<DatabaseCurrency>>()
    val historicalDetails: SharedFlow<List<DatabaseCurrency>> = _historicalDetails

    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    init {
        getHistoricalDetailsFromDB()
    }

    private fun getHistoricalDetailsFromDB() {
        viewModelScope.launch {
            _apiStatus.emit(Status.LOADING)
            _historicalDetails.emit(currencyLocalDataSourceImpl.get())
            Log.e("HistoricalDetailsViewModel","It Work")
            _apiStatus.emit(Status.SUCCESS)
        }
    }

}