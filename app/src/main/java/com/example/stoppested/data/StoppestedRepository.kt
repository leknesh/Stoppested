package com.example.stoppested.data


import com.example.stoppested.network.StoppestedApiService

class StoppestedRepository(private val stoppestedApiService: StoppestedApiService) {
    fun getStoppested(): String {
        return "Oslo S"
    }

    fun getDefaultStoppested(): Stoppested {
        return Stoppested(id = "42", name = "Oslo S", departures = emptyList())
    }

    suspend fun getDepartures(id: String) = stoppestedApiService.getDepartures(id)
}
