package com.nadimohammed.currencyconverter.util

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nadimohammed.currencyconverter.ui.currencyconverter.SpinnerAdapterData
import com.nadimohammed.currencyconverter.ui.details.HistoricalDetailsAdapter
import com.nadimohammed.currencyconverter.ui.othercurrencies.OtherCurrenciesAdapter
import com.nadimohammed.currencyconverter.ui.othercurrencies.OtherCurrencyRateAdapterData
import com.nadimohammed.data.db.DatabaseCurrency


object DataBindingAdapter {
    @BindingAdapter("bindSpinnerAdapter")
    @JvmStatic
    fun bindSpinnerAdapter(spinner: Spinner, sourceList: List<SpinnerAdapterData>?) {
        val categoryItems = ArrayList<String>()

        categoryItems.addAll(sourceList!!.map { it.currencyCountryCode })

        val spinnerAdapter = ArrayAdapter(
            spinner.context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            categoryItems
        )

        spinner.adapter = spinnerAdapter
    }

    @BindingAdapter("bindHistoricalDetailsRecycler")
    @JvmStatic
    fun RecyclerView.bindHistoricalDetailsRecycler(properties: List<DatabaseCurrency>?) {
        (adapter as HistoricalDetailsAdapter).submitList(properties)
    }

    @BindingAdapter("bindOtherCurrenciesRecycler")
    @JvmStatic
    fun RecyclerView.bindOtherCurrenciesRecycler(properties: List<OtherCurrencyRateAdapterData>?) {
        (adapter as OtherCurrenciesAdapter).submitList(properties)
    }


}
