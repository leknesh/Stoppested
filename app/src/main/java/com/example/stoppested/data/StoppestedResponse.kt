package com.example.stoppested.data

// contains the entire response from the geocoder api
data class StoppestedResponse(
    val geocoding: Geocoding,
    val type: String,
    val features: List<Feature>,
    val bbox: List<Double>
)

data class Geocoding(
    val version: String,
    val attribution: String,
    val query: Query,
    val engine: Engine,
    val timestamp: Long
)

data class Query(
    val text: String,
    val parser: String,
    val tokens: List<String>,
    val size: Int,
    val layers: List<String>,
    val sources: List<String>,
    val private: Boolean,
    val lang: Lang,
    val querySize: Int
)

data class Lang(
    val name: String,
    val iso6391: String,
    val iso6393: String,
    val defaulted: Boolean
)

data class Engine(
    val name: String,
    val author: String,
    val version: String
)

data class Feature(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val id: String,
    val gid: String,
    val layer: String,
    val source: String,
    val source_id: String,
    val name: String,
    val street: String,
    val accuracy: String,
    val country_a: String,
    val county: String,
    val county_gid: String,
    val locality: String,
    val locality_gid: String,
    val label: String,
    val category: List<String>
)
