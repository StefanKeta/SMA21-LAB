package com.example.smartwallet.model

import android.graphics.Color

object PaymentType {
    fun getColorFromPaymentType(type: String): Int {
        return if (type == "entertainment") Color.rgb(
            200,
            50,
            50
        ) else if (type == "food") Color.rgb(
            50,
            150,
            50)
        else if (type == "taxes") Color.rgb(
            20,
            20,
            150
        ) else if (type == "travel") Color.rgb(
            230,
            140,
            0)
        else Color.rgb(
            100,
            100,
            100)
    }
}