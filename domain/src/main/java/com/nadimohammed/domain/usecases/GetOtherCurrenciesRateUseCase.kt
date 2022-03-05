package com.nadimohammed.domain.usecases

import com.nadimohammed.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetOtherCurrenciesRateUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {

    suspend fun getOtherCurrencyRate() = currencyRepository.getOtherCurrencyRate()

}