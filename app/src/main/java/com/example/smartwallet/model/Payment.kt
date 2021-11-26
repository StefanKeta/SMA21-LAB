package com.example.smartwallet.model

data class Payment(
    var timestamp: String? = null,
    val cost : Double = 0.0,
    val name: String? = null,
    val type: String? = null,
)