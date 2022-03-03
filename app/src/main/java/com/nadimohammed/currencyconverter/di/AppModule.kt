package com.nadimohammed.currencyconverter.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nadimohammed.data.BuildConfig
import androidx.room.Room
import com.nadimohammed.data.api.CurrencyApi
import com.nadimohammed.data.db.CurrencyDAO
import com.nadimohammed.data.db.MainDatabase
import com.nadimohammed.data.repository.CurrencyRemoteDataSource
import com.nadimohammed.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun providesInterceptor(): Interceptor =
        Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().url(
                    chain.request().url.newBuilder()
                        .addQueryParameter("access_key", BuildConfig.API_KEY)
                        .build()
                ).build()
            )
        }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(providesInterceptor())
            .addInterceptor(providesLoggingInterceptor())
            .build()

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): MainDatabase =
        Room.databaseBuilder(context, MainDatabase::class.java, "MainDatabase")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesCurrencyDAO(db: MainDatabase): CurrencyDAO = db.currencyDAO

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCurrencyRepository(currencyRemoteDataSource: CurrencyRemoteDataSource): CurrencyRepository

}
