package com.nadimohammed.domain.usecases

import com.nadimohammed.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyRateUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {

    suspend fun getCurrencyRate() = currencyRepository.getCurrencyRate()

}

