package com.example.stoppested.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stoppested.data.Stoppested
import com.example.stoppested.data.StoppestedRepository
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

    // Oslo used as default location
    var locationState: LocationState by mutableStateOf(LocationState(osloLat, osloLong))
        private set

    private val _placeNameState = MutableStateFlow("")
    val placeNameState: StateFlow<String> get() = _placeNameState

    fun updateLocation() {
        viewModelScope.launch {
            try {
                val location = locationProvider.getCurrentLocation()
                if (location != null) {
                    locationState = LocationState(location.latitude, location.longitude)
                    Log.d("Stoppested", "Location: $location")
                }
            } catch (e: Exception) {
                Log.d("Stoppested", "Location error: $e")
            }
        }
    }

}

data class LocationState(val latitude: Double, val longitude: Double)

sealed interface StoppestedUiState {
    data class Loaded(val current: Stoppested) : StoppestedUiState
    data class Error(val message: String) : StoppestedUiState
    object Loading : StoppestedUiState
}
