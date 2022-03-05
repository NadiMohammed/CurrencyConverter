package com.nadimohammed.domain.repository

import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.entities.othercurrencyrate.OtherCurrencyRate

interface CurrencyRepository {
    suspend fun getCurrencyRate() : Result<CurrencyRateEntitie>

    suspend fun getOtherCurrencyRate() : Result<OtherCurrencyRate>
}