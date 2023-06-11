package com.example.dongsan2mong.event

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
    var str: String? = null


    constructor(int: Int) {
        this.int = int
    }
}