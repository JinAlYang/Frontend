package com.example.dongsan2mong.data

import android.os.Parcel
import android.os.Parcelable

data class HouseInfoData(
    // 매물 매매유형 : 전세, 월세
    val type: String = "월세",

    // 매물 가격 : 전세면 전세금만, 월세면 보증금 및 월세
    val price: String = "1000/60",

    // 매물 평형
    val space: String = "21.56m^2",

    // 매물 층수 : 반지하, 지상층(구체적으로 몇층인지도?), 옥탑
    val floor: String = "15층",

    // 매물 지역 : ㅁㅁ구 ㅁㅁ동
    val area: String = "광진구 화양동",

    // 매물 방 개수
    val roomNum: String = "오픈형 원룸",

    // 매물 이미지
    val imgURL: String = "",

    // 매물 위도 및 경도 정보
    val location: Array<Double> = arrayOf(0.0, 0.0)

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "월세",
        parcel.readString() ?: "1000/60",
        parcel.readString() ?: "21.56m^2",
        parcel.readString() ?: "15층",
        parcel.readString() ?: "광진구 화양동",
        parcel.readString() ?: "오픈형 원룸"
    )

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(type)
        parcel.writeString(price)
        parcel.writeString(space)
        parcel.writeString(floor)
        parcel.writeString(area)
        parcel.writeString(roomNum)
    }

    companion object CREATOR : Parcelable.Creator<HouseInfoData> {
        override fun createFromParcel(parcel: Parcel): HouseInfoData {
            return HouseInfoData(parcel)
        }

        override fun newArray(size: Int): Array<HouseInfoData?> {
            return arrayOfNulls(size)
        }
    }

}