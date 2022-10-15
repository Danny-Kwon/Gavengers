package com.example.gavengers.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetroService {
    @GET("/search/app")
    fun getDeviceListFromApi(): Call<DeviceListModel>

    companion object{
        val baseURL = "http://192.168.0.10:8080/"

        fun getRetroInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}