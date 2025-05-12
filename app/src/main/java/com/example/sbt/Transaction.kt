package com.example.sbt

data class Transaction(
    val name: String,
    val amount: String,
    val date: String,
    val isIncome: Boolean
)