package com.example.gavengers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gavengers.adapter.RetroViewAdapter
import com.example.gavengers.databinding.ActivityDbviewBinding
import com.example.gavengers.network.DeviceInfo
import com.example.gavengers.network.DeviceListModel
import com.example.gavengers.network.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DBViewActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityDbviewBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadAPIData()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.refresh.setOnClickListener{
            loadAPIData()
        }
    }

    private fun setAdapter(deviceList: ArrayList<DeviceInfo>) {
        val mAdapter = RetroViewAdapter(deviceList, applicationContext)
        binding.deviceRv.adapter = mAdapter
        binding.deviceRv.layoutManager = LinearLayoutManager(this)
    }

    fun loadAPIData(){
        val retrofitService = RetroService.getRetroInstance().create(RetroService::class.java)
        retrofitService.getDeviceListFromApi().enqueue(object : Callback<DeviceListModel>{
            override fun onResponse(
                call: Call<DeviceListModel>,
                response: Response<DeviceListModel>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let{
                        setAdapter(it.items)
                        Toast.makeText(applicationContext, "값 호출 성공", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<DeviceListModel>, t: Throwable) {
                t.message?.let { Log.d("API Error", it) }
            }
        })
    }
}