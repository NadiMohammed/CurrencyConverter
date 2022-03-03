package com.nadimohammed.data.repository

import com.nadimohammed.data.db.DatabaseCurrency
import com.nadimohammed.data.db.MainDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyLocalDataSourceImpl @Inject constructor(private val database: MainDatabase) {

    suspend fun create(currency: DatabaseCurrency) {
        database.currencyDAO.insert(currency)
    }

    suspend fun get(): List<DatabaseCurrency> {
        return database.currencyDAO.read()
    }


}