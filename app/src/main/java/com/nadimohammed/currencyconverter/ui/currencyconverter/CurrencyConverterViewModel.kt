package com.nadimohammed.currencyconverter.ui.currencyconverter

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nadimohammed.currencyconverter.R
import com.nadimohammed.currencyconverter.util.Status
import com.nadimohammed.data.db.DatabaseCurrency
import com.nadimohammed.data.repository.CurrencyLocalDataSourceImpl
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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val currencyLocalDataSourceImpl: CurrencyLocalDataSourceImpl
) : ViewModel() {

    //this val hold All Data Coming From Api
    private val _currencyRate = MutableSharedFlow<CurrencyRateEntitie>()

    //this val hold SpinnerAdapterData To Bind It To Spinner
    private val _spinnerData = MutableStateFlow(ArrayList<SpinnerAdapterData>())
    val spinnerData: StateFlow<ArrayList<SpinnerAdapterData>> = _spinnerData

    //This Handle All Our Api Request Status Like  LOADING, ERROR, SUCCESS
    private val _apiStatus = MutableSharedFlow<Status>()
    val apiStatus: SharedFlow<Status> = _apiStatus

    //This Handle All Our Errors
    private val _onMessageError = MutableSharedFlow<Exception>()
    val onMessageError: SharedFlow<Exception> = _onMessageError

    //toTxt is holding all data to show it on ToEditText automatically by observing on it and same for fromTxt
    var toTxt = MutableStateFlow<String?>("")
    var fromTxt = MutableStateFlow<String?>("1")

    //selectedCurrencyPriceFromSpinner is holding the currency rate of selected Counter on FromSpinner same for selectedCurrencyPriceToSpinner but it hold data of ToSpinner
    var selectedCurrencyPriceFromSpinner: Double = 0.0
    var selectedCurrencyPriceToSpinner: Double = 0.0

    //toSpinnerSelectedCountry is holding the Counter code of selected Counter on toSpinner same for fromSpinnerSelectedCountry but it hold data of fromSpinne
    var toSpinnerSelectedCountry: String = ""
    var fromSpinnerSelectedCountry: String = ""

    var switchToFrom = MutableStateFlow<Boolean?>(false)
    var switchFromTo = MutableStateFlow<Boolean?>(false)

    init {
        loadRemoteCurrencyRate()
    }

    /*
 * here we are handling the ToEditTextData by taking the number in toEditText and Multiply it in selectedCurrencyPriceToSpinner
 * */
    @SuppressLint("SimpleDateFormat")
    fun handleToEditTextData(number: Double?) {
        toTxt.value =
            (number.toString().toDouble() * selectedCurrencyPriceToSpinner).toString()

        saveLocalConvertedPriceToDB(
            DatabaseCurrency(
                SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()).toString(),
                fromSpinnerSelectedCountry,
                toSpinnerSelectedCountry,
                fromTxt.value.toString().toDouble(),
                toTxt.value.toString().toDouble()

            )
        )
    }

    /*
    * here we are handling the fromEditTextData by taking the number in toEditText and Multiply it in selectedCurrencyPriceFromSpinner
    * */
    @SuppressLint("SimpleDateFormat")
    fun handleFromEditTextData(number: Double?) {
        fromTxt.value =
            (number.toString().toDouble() * selectedCurrencyPriceFromSpinner).toString()

        saveLocalConvertedPriceToDB(
            DatabaseCurrency(
                SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()).toString(),
                fromSpinnerSelectedCountry,
                toSpinnerSelectedCountry,
                fromTxt.value.toString().toDouble(),
                toTxt.value.toString().toDouble()
            )
        )
    }

    /*
*  switchToFrom, switchFromTo is used to know when the user click on switch button if it was his first switch then we switch ToEditText value to fromTxt
* and we switch toSpinner Text To fromSpinner Text after that we calculate by take the data at fromEditText And Multiply it in the new currency value
* and when her press again the opposite happens
*/
    fun handleSwitchButtonClick(number: Double?) {
        if (switchToFrom.value == true && switchFromTo.value == false) { //First Switch
            toTxt.value =
                (number.toString().toDouble() * selectedCurrencyPriceFromSpinner).toString()
        }

        if (switchFromTo.value == true && switchToFrom.value == false) { //Second Switch
            toTxt.value =
                (number.toString().toDouble() * selectedCurrencyPriceToSpinner).toString()
        }
    }

    /*
* spinnerSelectedListener is spinner ItemSelectedListener it work every time spinner got change
* i use it to submit data to fromSpinner and toSpinner
* */
    val spinnerSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            when (parent!!.id) {
                R.id.fromSpinner -> {
                    if (switchToFrom.value == false && switchFromTo.value == false) {
                        selectedCurrencyPriceFromSpinner =
                            _spinnerData.value[position].currencyRate

                        fromSpinnerSelectedCountry =
                            _spinnerData.value[position].currencyCountryCode

                        toTxt.value =
                            _spinnerData.value[position].currencyRate.toString()

                    }
                }

                R.id.toSpinner -> {
                    if (switchToFrom.value == false && switchFromTo.value == false) {
                        selectedCurrencyPriceToSpinner = _spinnerData.value[position].currencyRate


                        toSpinnerSelectedCountry = _spinnerData.value[position].currencyCountryCode

                        toTxt.value =
                            _spinnerData.value[position].currencyRate.toString()


                    }
                }
            }
        }
    }


    /*
    *loadRemoteCurrencyRate() is used to get data from Api by sending to GetCurrencyRateUseCase And GetCurrencyRateUseCase send to CurrencyRepository
    * and CurrencyRepository request from CurrencyRemoteDataSourceImpl to execute getCurrencyRate() data
    * */
    private fun loadRemoteCurrencyRate() {
        viewModelScope.launch {
            _apiStatus.emit(Status.LOADING)

            try {
                when (val result = getCurrencyRateUseCase.getCurrencyRate()) {
                    is Result.Success -> {
                        _currencyRate.emit(result.results!!)

                        //we are converting incoming data from api to Json Se we can easily use it and we separate the currency from the country
                        val ratesDataToJsonString = Gson().toJson(result.results!!.rates)
                        val rateDataAsJsonObject = JSONObject(ratesDataToJsonString)
                        val spinnerAdapterData: ArrayList<SpinnerAdapterData> = ArrayList()

                        for (i in 0 until rateDataAsJsonObject.names().length()) {
                            spinnerAdapterData.add(
                                SpinnerAdapterData(
                                    (rateDataAsJsonObject.names().getString(i)),
                                    rateDataAsJsonObject.getDouble(
                                        rateDataAsJsonObject.names().getString(i)
                                    )
                                )
                            )
                        }

                        _spinnerData.value = spinnerAdapterData
                        _apiStatus.emit(Status.SUCCESS)
                    }

                    is Result.Failed -> {
                        _apiStatus.emit(Status.ERROR)
                    }
                }

            } catch (e: Exception) {
                _onMessageError.emit(e)
                _apiStatus.emit(Status.ERROR)
            }

        }
    }

    /*
    *here we are saving converted Data To our Room Database
    *   */
    private fun saveLocalConvertedPriceToDB(databaseCurrency: DatabaseCurrency) {
        viewModelScope.launch {
            currencyLocalDataSourceImpl.create(databaseCurrency)
        }
    }

}