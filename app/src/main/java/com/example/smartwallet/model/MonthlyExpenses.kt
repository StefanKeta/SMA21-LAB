package com.example.smartwallet.model

data class MonthlyExpenses(var month:String, val expenses: Int, val income: Int){
    constructor(): this("",0,0)
}