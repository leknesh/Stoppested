package com.example.stoppested.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val networkModule = module {
    single<ApolloClient> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        ApolloClient.Builder()
            .serverUrl("https://api.entur.io/journey-planner/v3/graphql")
            .okHttpClient(okHttpClient)
            .addHttpHeader("ET-Client-Name", "leknesh-stoppested")
            .build()
    }
}