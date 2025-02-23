package com.example.stoppested.ui.screens

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stoppested.data.Stoppested
import com.example.stoppested.data.StoppestedRepository
import com.example.stoppested.data.StoppestedSuggestion
import com.example.stoppested.data.getFilteredDepartures
import com.example.stoppested.data.toStoppested
import com.example.stoppested.data.toSuggestions
import com.example.stoppested.location.LocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoppestedViewModel(
    private val stoppRepository: StoppestedRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val osloLat = 59.91
    private val osloLong = 10.75

    var uiState: StoppestedUiState by mutableStateOf(StoppestedUiState.Loading)
        private set

    var locationState: LocationState? by mutableStateOf(null)
        private set

    private val _suggestionsState = MutableStateFlow(listOf<StoppestedSuggestion>())
    val suggestionsState: StateFlow<List<StoppestedSuggestion>> get() = _suggestionsState

    fun updateLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                locationState = if (location != null) {
                    LocationState(location.latitude, location.longitude)
                } else {
                    LocationState(osloLat, osloLong)
                }
                updateStoppestedAndDepartures()
            } catch (e: Exception) {
                Log.d("Stoppested", "Location error: $e")
            }
        }
    }

    fun searchDeparturesForStoppested(selectedStopId: String) {
        viewModelScope.launch {
            uiState = try {
                val departures = stoppRepository.getDepartures(selectedStopId).stopPlace?.toStoppested()
                val filtered = departures?.getFilteredDepartures() ?: emptyList()
                StoppestedUiState.Loaded(departures?.copy(departures = filtered))
            } catch (e: Exception) {
                Log.d("Stoppested", "Load error: $e")
                StoppestedUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun updateStoppestedAndDepartures() {
        viewModelScope.launch {
            try {
                locationState?.let { state ->
                    val stoppestedResponse = stoppRepository.getStoppestedNearby(state.latitude, state.longitude)
                    val stoppested = stoppestedResponse.features.firstOrNull()?.properties?.id
                    if (stoppested != null) {
                        val departures = stoppRepository.getDepartures(stoppested).stopPlace?.toStoppested()
                        uiState = StoppestedUiState.Loaded(departures)
                    } else {
                        uiState = StoppestedUiState.Error("No stoppested found")
                    }
                } ?: run {
                    uiState = StoppestedUiState.Error("Location is null")
                }
            } catch (e: Exception) {
                Log.d("Stoppested", "Error: $e")
                uiState = StoppestedUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun searchLocationAutocomplete(query: String) {
        viewModelScope.launch {
            try {
                val stoppestedResponse = stoppRepository.getStoppestedAutocomplete(query)
                _suggestionsState.value = stoppestedResponse.toSuggestions()
            } catch (e: Exception) {
                Log.d("Stoppested", "Error: $e")
            }
        }
    }

    private fun calculateDistance(location1: Location, location2: Location): String {
        val distanceInMeters = location1.distanceTo(location2)
        return if (distanceInMeters < 1000) {
            "${distanceInMeters.toInt()} m"
        } else {
            String.format("%.1f km", distanceInMeters / 1000)
        }
    }

    fun getDistance(stoppested: Stoppested): String {
        return if (locationState != null && stoppested.latitude != null && stoppested.longitude != null) {
            val userLocation = Location("userLocation").apply {
                latitude = locationState!!.latitude
                longitude = locationState!!.longitude
            }
            val stoppestedLocation = Location("stoppested").apply {
                latitude = stoppested.latitude
                longitude = stoppested.longitude
            }
            calculateDistance(userLocation, stoppestedLocation)
        } else {
            "N/A"
        }
    }
}

data class LocationState(val latitude: Double, val longitude: Double)

sealed interface StoppestedUiState {
    data class Loaded(val current: Stoppested?) : StoppestedUiState
    data class Error(val message: String) : StoppestedUiState
    object Loading : StoppestedUiState
}
