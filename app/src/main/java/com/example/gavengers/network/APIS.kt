package com.example.gavengers.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.*

interface APIS {
    @POST("register/user") // 카카오 로그인시 사용자 PK값 전송 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun registerUser(
        @Body jsonparams: User
    ): Call<OkSign>

    @POST("register/userdevice") // 어플내에서 DeviceId 입력 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun registerUserdevice(
        @Body jsonparams: UserDevice
    ): Call<OkSign>

    @POST("delete/user") // 회원 탈퇴시 API, DeviceId 도 전체 다 삭제됨 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun deleteUser(
        @Body jsonparams: User
    ): Call<OkSign>

    @POST("delete/user/option") // 선택한 DeviceId 만 삭제됨 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun deleteUserOption(
        @Body jsonparams: UserDevice
    ): Call<OkSign>

    @POST("search/rxbattery") // rx배터리 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun searchRx(
        @Body jsonparams: Device
    ): Call<Rx>

    @POST("search/txbattery") // tx배터리 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun searchTx(
        @Body jsonparams: Device
    ): Call<Tx>

    @POST("search/power") // 전원 유무 확인 (성공)
    @Headers("accept: application/json",
        "content-type: application/json")
    fun searchPower(
        @Body jsonparams: Device
    ): Call<Power>

    @POST("search/app") // 출입 정보 및 시간정보 확인
    @Headers("accept: application/json",
        "content-type: application/json")
    fun searchApp(
        @Body jsonparams: Device
    ): Call<ArrayList<Sensing>>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://124.60.219.83:8080/" // 주소

        fun create(): APIS {
            val gson :Gson = GsonBuilder().setLenient().create()
            val ret :APIS= Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
            Log.d("ret=", ret.toString())
            return ret
        }
    }
}