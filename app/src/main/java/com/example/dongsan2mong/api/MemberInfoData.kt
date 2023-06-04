package com.example.dongsan2mong.api

data class MemberInfoData(
    val id: Int = 20010626,
    val name: String = "홍길동",
    val email: String = "mg010626@naver.com",
    val gender: String = "남",
    val age: Int = 23,
    val livingExpenses: Int = 500,
    val savingMoney: Int = 500,
    val loanInterest: Int = 5
)
