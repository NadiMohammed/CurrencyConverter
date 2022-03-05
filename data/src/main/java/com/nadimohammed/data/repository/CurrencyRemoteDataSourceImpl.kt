package com.nadimohammed.data.repository

import com.nadimohammed.data.CallbackHandler
import com.nadimohammed.data.api.CurrencyApi
import com.nadimohammed.domain.Result

import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.entities.othercurrencyrate.OtherCurrencyRate
import com.nadimohammed.domain.repository.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteDataSourceImpl @Inject constructor(private val currencyApi: CurrencyApi) :
    CurrencyRepository, CallbackHandler() {

    override suspend fun getCurrencyRate(): Result<CurrencyRateEntitie> {
        return safeApiCall { currencyApi.getCurrencyRate() }
    }

    override suspend fun getOtherCurrencyRate(): Result<OtherCurrencyRate> {
        return safeApiCall { currencyApi.getOtherCurrencyRate() }
    }

}
