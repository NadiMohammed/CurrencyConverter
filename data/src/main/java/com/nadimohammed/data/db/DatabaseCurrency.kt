package com.nadimohammed.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class DatabaseCurrency(
    @PrimaryKey
    var day: String = "",
    var fromCurrencyCode: String = "",
    var toCurrencyCode: String = "",
    var fromCurrencyAmount: Double? = 0.0,
    var toCurrencyConverted: Double? = 0.0,
)