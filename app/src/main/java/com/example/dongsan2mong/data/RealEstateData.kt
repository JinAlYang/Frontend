package com.example.dongsan2mong.data

import java.io.Serializable

data class RealEstateData(
    val id: Int = 0,
    val imgUrl: String = "",
    val areaNumberAddress: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val monthlyRentType: String = "월세",
    val deposit: Int = 0,
    val monthlyPayment: Int = 0,
    val roomSize: Double = 0.0,
    val spaceType: String = "",
    val occupancyPeriods: String = "",
    val windowDirection: String = "",
    val builtDate: String = ""
) : Serializable
