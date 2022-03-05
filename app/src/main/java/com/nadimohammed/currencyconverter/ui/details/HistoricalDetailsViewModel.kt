package com.nadimohammed.currencyconverter.ui.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.data.db.DatabaseCurrency
import com.nadimohammed.data.repository.CurrencyLocalDataSourceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricalDetailsViewModel @Inject constructor(private val currencyLocalDataSourceImpl: CurrencyLocalDataSourceImpl) :
    ViewModel() {

    private val _historicalDetails = MutableStateFlow<List<DatabaseCurrency>>(emptyList())
    val historicalDetails: StateFlow<List<DatabaseCurrency>> = _historicalDetails

    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    private val _onMessageError = MutableSharedFlow<Exception>()
    val onMessageError: SharedFlow<Exception> = _onMessageError

    init {
        getLocalHistoricalDetailsFromDB()
    }

    /*
    * here i request from Room Database to get saved Converted currencies
    * */
    private fun getLocalHistoricalDetailsFromDB() {
        viewModelScope.launch {
            try {
                _apiStatus.emit(Status.LOADING)
                _historicalDetails.emit(currencyLocalDataSourceImpl.get())
                _apiStatus.emit(Status.SUCCESS)
            } catch (e: Exception) {
                _onMessageError.emit(e)
                _apiStatus.emit(Status.ERROR)
            }
        }

    }

}