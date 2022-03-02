package com.nadimohammed.data.repository

import com.nadimohammed.data.CallbackHandler
import com.nadimohammed.data.db.MainDatabase
import com.nadimohammed.domain.entities.currencyrate.CurrencyRateEntitie
import com.nadimohammed.domain.repository.CurrencyLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyLocalDataSourceImpl @Inject constructor(private val database: MainDatabase) :
    CurrencyLocalRepository, CallbackHandler() {

    override suspend fun create(currency: List<CurrencyRateEntitie>) {
        database.currencyDAO.insert()
    }

    override suspend fun get(): List<CurrencyRateEntitie> = database.currencyDAO.get()

}