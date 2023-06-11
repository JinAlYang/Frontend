package com.example.dongsan2mong.event

import android.util.Log
import com.example.dongsan2mong.data.HouseInfoData

class DataEvent {
    /*
    int
    0 -> mapFragment to ClusterActivity
    1 -> ClusterActivity to mapFragment
    2 -> mapFragment to wishlist
    3 -> wishlist to mapFragment
    4 -> wishlist to latesthome
    5 -> latesthome to wishlist
    6 -> wishlist to dibshome
    7 -> dibshome to wishlist
    8 -> latesthome to dibshome
    9 -> dibshome to latesthome

     */

    var int: Int? = 0
    var dibsArr: ArrayList<HouseInfoData> = ArrayList()

    constructor(int: Int) {
        this.int = int
    }

    constructor(int: Int, arr: ArrayList<HouseInfoData>) {
        this.int = int
        this.dibsArr = arr
        Log.e("dataEvent", "dibs Arr size : ${this.dibsArr.size}")
    }
}