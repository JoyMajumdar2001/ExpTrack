package com.qdot.exptrackapp.models

data class ExpenseModel (
    val id : Int,
    val amount : Float,
    val type : String,
    val note : String,
    val time : String,
    val day : Int,
    val week : Int,
    val month : Int,
    val year : Int,
    val userid : String,
)