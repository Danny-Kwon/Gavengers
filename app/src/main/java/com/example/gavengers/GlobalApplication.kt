package com.example.gavengers

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "4def035fa944e9fc45fc4eb007a01354")
    }
}