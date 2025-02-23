package com.example.stoppested.data


import com.example.stoppested.network.DepartureApiService
import com.example.stoppested.network.GeocoderApiService

class StoppestedRepository(
    private val departureApiService: DepartureApiService,
    private val geocoderApiService: GeocoderApiService
) {
    suspend fun getDepartures(id: String) = departureApiService.getDepartures(id)

    suspend fun getStoppestedAutocomplete(text: String) = geocoderApiService.getStoppestedAutocomplete(text)

    suspend fun getStoppestedNearby(latitude: Double, longitude: Double) = geocoderApiService.getStoppestedNearby(latitude, longitude)
}
