package com.example.stoppested.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.stoppested.data.Stoppested
import com.example.stoppested.data.StoppestedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoppestedViewModel(
    private val stoppRepository: StoppestedRepository,
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

}

data class LocationState(val latitude: Double, val longitude: Double)

sealed interface StoppestedUiState {
    data class Loaded(val current: Stoppested) : StoppestedUiState
    data class Error(val message: String) : StoppestedUiState
    object Loading : StoppestedUiState
}
