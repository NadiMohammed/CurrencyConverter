package data.api

import com.nadimohammed.domain.entities.currencyrate.CurrencyRate
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {
    @GET("latest")
    suspend fun getCurrencyRate(): Response<CurrencyRate>

}