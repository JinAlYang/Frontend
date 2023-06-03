package com.example.dongsan2mong.data


import android.os.Parcel
import android.os.Parcelable

data class FreesetData(
    val region: String = "지역",
    var typeOfSale: String = "매매유형",
    var numOfRooms: Int = 0,
    var amenities: String = "편의시설",
    var buildingType: String = "건물유형",
    var roomArea: Int = 0,
    var availableDate: String = "입주가능일시",
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
        parcel.writeString(availableDate)
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