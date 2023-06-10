package com.example.dongsan2mong.data

data class BoundaryBoxData(
    var houseCount: Int = 0
) {

    fun getCount(): Int {
        return houseCount
    }

}
