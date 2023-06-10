package com.example.dongsan2mong.data

import android.os.Parcel
import android.os.Parcelable

data class BoundaryBoxData(
    var houseCount: Int = 0,
    val houseInfo: HouseInfoData
) {

    fun getCount(): Int {
        return houseCount
    }

}
