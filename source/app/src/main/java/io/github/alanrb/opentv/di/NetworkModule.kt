package io.github.alanrb.opentv.di

import io.github.alanrb.opentv.BuildConfig
import io.github.alanrb.opentv.data.api.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

val networkModule = module {

    single<TraktService> { createWebService(get(), get(), "https://api.trakt.tv/") }
    single<TmdbService> { createWebService(get(), get(), "https://api.themoviedb.org/3/") }

    single { createOkHttpClient(get()) }

    single { AuthInterceptor() }

    single<NetworkFailureDispatcher> { NetworkFailure.NetworkFailureDispatcherImpl() }
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    failureDispatcher: NetworkFailureDispatcher,
    url: String
): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(nullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ResultCallAdapterFactory(failureDispatcher))
        .build()
        .create(T::class.java)
}

fun createOkHttpClient(
    authInterceptor: AuthInterceptor,
): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
        )
        .build()

fun nullOnEmptyConverterFactory() = object : Converter.Factory() {

    fun converterFactory() = this

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ) = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter =
            retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

        override fun convert(value: ResponseBody) =
            if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
    }
}