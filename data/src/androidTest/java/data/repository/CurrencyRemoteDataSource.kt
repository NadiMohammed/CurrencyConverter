package data.repository

import com.nadimohammed.domain.Result
import com.nadimohammed.domain.entities.currencyrate.CurrencyRate
import com.nadimohammed.domain.repository.CurrencyRepository
import data.CallbackHandler
import data.api.CurrencyApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRemoteDataSource @Inject constructor(private val currencyApi: CurrencyApi) :
    CurrencyRepository, CallbackHandler() {

    override suspend fun getCurrencyRate(): Result<CurrencyRate> {
        return safeApiCall { currencyApi.getCurrencyRate() }
    }

}
