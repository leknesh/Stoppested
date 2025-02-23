package com.example.stoppested.network

import com.example.stoppested.data.StoppestedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderApiService {

    @GET("autocomplete")
    suspend fun getStoppestedAutocomplete(
        @Query("text") text: String,
        @Query("size") size: Int = 10,
        @Query("lang") lang: String = "no",
        @Query("layers") layers: String = "venue",     // the "venue" layer seems to be the filter for stops
    ): StoppestedResponse

    @GET("reverse")
    suspend fun getStoppestedNearby(
        @Query("point.lat") latitude: Double,
        @Query("point.lon") longitude: Double,
        @Query("boundary.circle.radius") radius: Int = 1,
        @Query("size") size: Int = 1,
        @Query("layers") layers: String = "venue",     // the "venue" layer seems to be the filter for stops
        @Query("lang") lang: String = "no"
    ): StoppestedResponse
}