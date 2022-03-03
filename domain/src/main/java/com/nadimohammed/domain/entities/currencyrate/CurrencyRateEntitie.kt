package com.nadimohammed.domain.entities.currencyrate

data class CurrencyRateEntitie(
    val base: String? = "",
    val date: String? = "",
    val rates: Rates? = Rates(),
    val success: Boolean? = false,
    val timestamp: Int? = 0
)