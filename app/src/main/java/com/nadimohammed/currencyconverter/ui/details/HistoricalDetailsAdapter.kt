package com.nadimohammed.currencyconverter.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nadimohammed.currencyconverter.databinding.ItemHistoricalDetailsBinding
import com.nadimohammed.data.db.DatabaseCurrency

class HistoricalDetailsAdapter :
    ListAdapter<DatabaseCurrency, HistoricalDetailsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoricalDetailsBinding.inflate(
                 LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemHistoricalDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DatabaseCurrency) {
            binding.apply {
                dayTxt.text = item.day
                fromCurrencyCodeTxt.text = item.fromCurrencyCode
                fromCurrencyAmountTxt.text = item.fromCurrencyAmount.toString()
                toCurrencyCodeTxt.text = item.toCurrencyCode
                toCurrencyConvertedTxt.text = item.toCurrencyConverted.toString()
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<DatabaseCurrency>() {
    override fun areItemsTheSame(
        oldSearch: DatabaseCurrency,
        newSearch: DatabaseCurrency
    ): Boolean {
        return oldSearch == newSearch
    }

    override fun areContentsTheSame(
        oldSearch: DatabaseCurrency,
        newSearch: DatabaseCurrency
    ): Boolean {
        return oldSearch == newSearch
    }
}