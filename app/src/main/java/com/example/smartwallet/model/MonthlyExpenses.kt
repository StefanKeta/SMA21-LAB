package com.example.smartwallet.model

data class MonthlyExpenses(var month:String, val expenses: Double, val income: Double){
    constructor(): this("",0.00,0.00)
}