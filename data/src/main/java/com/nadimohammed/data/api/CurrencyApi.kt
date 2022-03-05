package com.nadimohammed.data.api


import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.entities.othercurrencyrate.OtherCurrencyRate
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("latest")
    suspend fun getCurrencyRate(): Response<CurrencyRateEntitie>

    @GET("latest")
    suspend fun getOtherCurrencyRate(): Response<OtherCurrencyRate>
}