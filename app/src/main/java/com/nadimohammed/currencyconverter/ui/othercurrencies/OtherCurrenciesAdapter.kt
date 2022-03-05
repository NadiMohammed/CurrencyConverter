package com.nadimohammed.currencyconverter.ui.othercurrencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nadimohammed.currencyconverter.databinding.ItemOtherCurrenciesBinding

class OtherCurrenciesAdapter :
    ListAdapter<OtherCurrencyRateAdapterData, OtherCurrenciesAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOtherCurrenciesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemOtherCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OtherCurrencyRateAdapterData) {

            binding.apply {
                currencyPriceTxt.text = (item.currencyRate.toString().toDouble()).toString()
                countryCodeTxt.text = item.countryCode

            }
        }
    }

}

private class DiffCallback : DiffUtil.ItemCallback<OtherCurrencyRateAdapterData>() {
    override fun areItemsTheSame(
        oldSearch: OtherCurrencyRateAdapterData,
        newSearch: OtherCurrencyRateAdapterData
    ): Boolean {
        return oldSearch == newSearch
    }

    override fun areContentsTheSame(
        oldSearch: OtherCurrencyRateAdapterData,
        newSearch: OtherCurrencyRateAdapterData
    ): Boolean {
        return oldSearch == newSearch
    }
}