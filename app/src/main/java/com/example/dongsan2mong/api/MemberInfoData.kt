package com.example.dongsan2mong.api

data class MemberInfoData(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int,
    val livingExpenses: Int,
    val savingMoney: Int,
    val loanInterest: Int
)
