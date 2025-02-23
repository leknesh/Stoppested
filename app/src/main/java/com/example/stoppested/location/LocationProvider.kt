package com.example.stoppested.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationProvider(
    private val context: Context
) {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        return withContext(Dispatchers.IO) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val location = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                    bestLocation = location
                }
            }
            bestLocation
        }
    }
}