package com.example.dongsan2mong.data

import android.os.Parcel
import android.os.Parcelable
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
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imgUrl)
        parcel.writeString(areaNumberAddress)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(monthlyRentType)
        parcel.writeInt(deposit)
        parcel.writeInt(monthlyPayment)
        parcel.writeDouble(roomSize)
        parcel.writeString(spaceType)
        parcel.writeString(occupancyPeriods)
        parcel.writeString(windowDirection)
        parcel.writeString(builtDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RealEstateData> {
        override fun createFromParcel(parcel: Parcel): RealEstateData {
            return RealEstateData(parcel)
        }

        override fun newArray(size: Int): Array<RealEstateData?> {
            return arrayOfNulls(size)
        }
    }
}
