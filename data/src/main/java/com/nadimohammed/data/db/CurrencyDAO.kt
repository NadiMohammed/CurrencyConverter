package com.nadimohammed.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(databaseCurrency: DatabaseCurrency)

    @Query("SELECT * FROM currency")
    suspend fun read(): List<DatabaseCurrency>
}