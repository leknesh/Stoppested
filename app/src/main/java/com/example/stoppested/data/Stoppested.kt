package com.example.stoppested.data

import com.example.StopPlaceQuery
import com.example.stoppested.R

data class Stoppested (
    val id: String,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    val departures: List<Departure>
)

data class Departure(
    val lineName: String,
    val lineCode: String,
    val scheduledDeparture: String,
    val expectedDeparture: String? = null,
    val transportType: TransportType

)

enum class TransportType {
    RAIL, BUS, TRAM, METRO, FERRY
}

// unknown whether these are the only possible spellings and types
fun String.toTransportType() : TransportType {
    return when(this) {
        "rail" -> TransportType.RAIL
        "bus" -> TransportType.BUS
        "tram" -> TransportType.TRAM
        "metro" -> TransportType.METRO
        "ferry", "water" -> TransportType.FERRY
        else -> TransportType.BUS
    }
}

fun TransportType.getDrawable() : Int {
    return when(this) {
        TransportType.RAIL -> R.drawable.baseline_train_24
        TransportType.BUS -> R.drawable.baseline_directions_bus_24
        TransportType.TRAM -> R.drawable.baseline_tram_24
        TransportType.METRO -> R.drawable.baseline_subway_24
        TransportType.FERRY -> R.drawable.baseline_directions_boat_24
    }
}

// mapper from the StopPlace response stoppested with departures
fun StopPlaceQuery.StopPlace.toStoppested(): Stoppested {
    return Stoppested(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        departures = this.estimatedCalls.map { it.toDeparture() }
    )
}

// not sure whether aimeddeparture or expecteddeparture is the correct one
fun StopPlaceQuery.EstimatedCall.toDeparture(): Departure {
    return Departure(
        lineName = destinationDisplay?.frontText ?: "Bloksberg",
        lineCode = serviceJourney.journeyPattern?.line?.id?.substringAfterLast(":") ?: "42",
        scheduledDeparture = aimedDepartureTime.toString().substring(11, 16),                // not handling dates p.t
        expectedDeparture = expectedDepartureTime.toString().substring(11, 16),
        transportType = serviceJourney.journeyPattern?.line?.transportMode?.toString()?.toTransportType() ?: TransportType.BUS
    )
}




