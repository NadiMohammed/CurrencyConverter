package com.nadimohammed.domain.repository

import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRate

interface CurrencyRepository {
    suspend fun getCurrencyRate() : Result<CurrencyRate>
}