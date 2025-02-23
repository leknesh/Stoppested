package com.example.stoppested.data

data class StoppestedSuggestion(val id: String, val name: String)

fun StoppestedResponse.toSuggestions(): List<StoppestedSuggestion> {
    return features.map { StoppestedSuggestion(it.properties.id, it.properties.name) }
}