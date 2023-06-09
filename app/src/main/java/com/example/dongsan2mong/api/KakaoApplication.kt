package com.example.dongsan2mong.api

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "c70148f669794ab2955b93e869d224c4")
    }
}