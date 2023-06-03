package com.example.dongsan2mong.data


import android.os.Parcel
import android.os.Parcelable

data class FreesetData(
    // 지역 : ㅁㅁ구 ㅇㅇ동
    val region: String = "지역",

    // 매매유형 : 전세, 월세, 보증금, 월세
    var typeOfSale: String = "매매유형",

    // 방 갯수 : 값이 곧 방 갯수
    var numOfRooms: Int = 0,

    // 편의시설 : 엘레베이터, 편의점, 세탁소, 대형마트 등
    var amenities: String = "편의시설",

    // 건물 유형 : 원룸, 투룸, 오피스텔 중 하나
    var buildingType: String = "건물유형",

    // 평형 :
    var roomArea: Int = 0,

    // 층수 : 반지하, 지상층(구체적으로 몇층인지도?), 옥탑
    var roomFloor: String = "지상층",

    // 싱크대, 에어컨, 세탁기, 냉장고 등
    var options: String = "옵션",

    var freesetTitle: String = "프리셋 제목"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "지역",
        parcel.readString() ?: "매매유형",
        parcel.readInt(),
        parcel.readString() ?: "편의시설",
        parcel.readString() ?: "건물유형",
        parcel.readInt(),
        parcel.readString() ?: "입주가능일시",
        parcel.readString() ?: "옵션",
        parcel.readString() ?: "프리셋 제목"
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(region)
        parcel.writeString(typeOfSale)
        parcel.writeInt(numOfRooms)
        parcel.writeString(amenities)
        parcel.writeString(buildingType)
        parcel.writeInt(roomArea)
        parcel.writeString(roomFloor)
        parcel.writeString(options)
        parcel.writeString(freesetTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FreesetData> {
        override fun createFromParcel(parcel: Parcel): FreesetData {
            return FreesetData(parcel)
        }

        override fun newArray(size: Int): Array<FreesetData?> {
            return arrayOfNulls(size)
        }
    }
}