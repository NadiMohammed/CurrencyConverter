package com.nadimohammed.data.db

import androidx.room.*

@Dao
interface CurrencyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(databaseCurrency: List<DatabaseCurrency>)

    @Query("SELECT * FROM currency")
    suspend fun get(): List<DatabaseCurrency>
}