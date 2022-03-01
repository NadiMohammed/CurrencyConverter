package com.nadimohammed.domain.repository

import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie

interface CurrencyRepository {
    suspend fun getCurrencyRate() : Result<CurrencyRateEntitie>
}