package com.example.stoppested.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.entur.io/geocoder/v1/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GeocoderApiService> {
        get<Retrofit>().create(GeocoderApiService::class.java)
    }

    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://api.entur.io/journey-planner/v3/graphql")
            .okHttpClient(get())
            .addHttpHeader("ET-Client-Name", "leknesh-stoppested")
            .build()
    }

    single<DepartureApiService> { DepartureApiService(stoppestedApolloClient = get())}
}