package com.example.dongsan2mong.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api: RetrofitService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jinalyang-fly.fly.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}