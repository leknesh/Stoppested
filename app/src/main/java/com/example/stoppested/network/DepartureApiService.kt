package com.example.stoppested.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.example.StopPlaceQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DepartureApiService(
    private val stoppestedApolloClient: ApolloClient
) {
    suspend fun getDepartures(id: String): StopPlaceQuery.Data {
        val response: ApolloResponse<StopPlaceQuery.Data> = withContext(Dispatchers.IO) {
            stoppestedApolloClient.query(StopPlaceQuery(id = id)).execute()
        }
        return response.dataAssertNoErrors
    }
}

