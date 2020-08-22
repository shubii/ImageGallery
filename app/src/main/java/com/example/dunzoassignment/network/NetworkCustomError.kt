package com.example.dunzoassignment.network

data class NetworkCustomError(
    val msg: String = "Something went wrong.!!",
    val errorInfo: String = "",
    val status: Int = 500
)