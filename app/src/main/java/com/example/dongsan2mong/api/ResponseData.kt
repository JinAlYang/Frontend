package com.example.dongsan2mong.api

data class ResponseData(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int
)

data class memberInfo(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int,
    val livingExpenses: Int,
    val savingMoney: Int,
    val loanInterest: Int
)

data class wishListInfo(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int
)

data class realEstate(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int
)

data class realEstateDetail(
    val realEstateDetailId: Int,
    val realEstateId: Int,
    val managementFees: Int,

    // 집 옵션
    val sinkStatus: Boolean,
    val airConditionStatus: Boolean,
    val showBoxStatus: Boolean,
    val washingMachineStatus: Boolean,
    val refrigeratorStatus: Boolean,
    val closetStatus: Boolean,
    val gasStovesStatus: Boolean,
    val bedStatus: Boolean,

    // 주변 생활시설
    val cafeStatus: Boolean,
    val convenienceStoreStatus: Boolean,
    val laundryStatus: Boolean,
    val supermarketStatus: Boolean,
    val busStationStatus: Boolean,
    val subwayStatus: Boolean,

    val detailInfo: String,
)

data class presetResponse(
    val preset: List<presetInfo>
)

data class presetInfo(
    val id: Int,
    val name: String,
    val email: String,
    val gender: String,
    val age: Int
)
