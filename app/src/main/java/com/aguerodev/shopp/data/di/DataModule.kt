package com.aguerodev.shopp.data.di

import android.content.Context
import androidx.room.Room
import com.aguerodev.shopp.data.datasource.database.AppDataBase
import com.aguerodev.shopp.data.datasource.network.ShoppClientCountryA
import com.aguerodev.shopp.data.datasource.network.ShoppClientCountryB
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Singleton
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val SHOPP_DB = "shopp_db"
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CountryAApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CountryBApi

    @CountryAApi
    @Singleton
    @Provides
    fun provideShoppClientCountryA(
        @CountryAApi retrofit: Retrofit
    ): ShoppClientCountryA {
        return retrofit.create(ShoppClientCountryA::class.java)
    }

    @CountryAApi
    @Singleton
    @Provides
    fun provideRetrofitCountryA(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @CountryBApi
    @Singleton
    @Provides
    fun provideShoppClientCountryB(
        @CountryBApi retrofit: Retrofit
    ): ShoppClientCountryB {
        return retrofit.create(ShoppClientCountryB::class.java)
    }

    @CountryBApi
    @Singleton
    @Provides
    fun provideRetrofitCountryB(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/")
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    fun provideJson(): Json{
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        SHOPP_DB
    ).build()

    @Provides
    @Singleton
    fun provideShoppDao(database: AppDataBase) = database.shoppDao()

}