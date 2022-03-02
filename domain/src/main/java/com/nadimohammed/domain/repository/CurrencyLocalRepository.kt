package com.nadimohammed.domain.repository

import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie

interface CurrencyLocalRepository {
    suspend fun create(currency: List<CurrencyRateEntitie>)

    suspend fun get(): List<CurrencyRateEntitie>
}


