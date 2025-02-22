package com.example.stoppested.data

class StoppestedRepository(private val input: String = "Hello, World!") {
    fun getStoppested(): String {
        return input
    }

    fun getDefaultStoppested(): Stoppested {
        return Stoppested(id = "42", name = "Oslo S")
    }
}
