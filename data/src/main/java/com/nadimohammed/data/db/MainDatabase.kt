package com.nadimohammed.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext


@Database(entities = [DatabaseCurrency::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract val currencyDAO: CurrencyDAO

    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(@ApplicationContext context: Context): MainDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "MainDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}