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
    @POST("signUp/")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun post_users(
        @Body jsonparams: UserDevice
    ): Call<Responsed>

    @POST("login/")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun login(
        @Body jsonparams: User
    ): Call<Responsed>

    @POST("closeAccount/")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun close_account(
        @Body jsonparams: User
    ): Call<Responsed>

    @POST("deleteDevice/")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun delete_device(
        @Body jsonparams: UserDevice
    ): Call<Responsed>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://192.168.0.10:8080/" // 주소

        fun create(): APIS {
            val gson :Gson =   GsonBuilder().setLenient().create();
            val ret : APIS = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
            Log.d("ret=", ret.toString())
            return ret
        }
    }
}