package com.example.gavengers.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gavengers.adapter.RetroViewAdapter
import com.example.gavengers.databinding.ActivityDbviewBinding
import com.example.gavengers.network.*
import com.example.gavengers.sharedpreferences.PreferencesUtil
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
        val prefs = PreferencesUtil(applicationContext)
        val devID = prefs.getString("ConnectedID", "ID Error")
        val api = APIS.create()
        loadAPIData()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.batteryButton.setOnClickListener{ // 송신 및 수신 배터리
            val data = Device(deviceId = devID)
            api.searchRx(data).enqueue(object: Callback<Rx>{ // 수신 배터리
                override fun onResponse(call: Call<Rx>, response: Response<Rx>) {
                    if(response.body().toString().isNotEmpty()){
                        prefs.setString("RX", response.body()?.rx.toString())
                    }
                }
                override fun onFailure(call: Call<Rx>, t: Throwable) {
                    Log.d("RxBattery", "Error")
                }
            })
            api.searchTx(data).enqueue(object: Callback<Tx>{ // 송신 배터리
                override fun onResponse(call: Call<Tx>, response: Response<Tx>) {
                    if(response.body().toString().isNotEmpty()){
                        prefs.setString("TX", response.body()?.tx.toString())
                    }
                }
                override fun onFailure(call: Call<Tx>, t: Throwable) {
                    Log.d("TxBattery", "Error")
                }
            })
            Toast.makeText(this,
                "송신 기기 배터리: ${prefs.getString("TX", "TX Error")}, 수신 기기 배터리: ${prefs.getString("RX", "RX Error")}",
                Toast.LENGTH_SHORT).show()
        }

        binding.powerButton.setOnClickListener{ // 전원 여부
            val data = Device(deviceId = devID)
            api.searchPower(data).enqueue(object: Callback<Power>{
                override fun onResponse(call: Call<Power>, response: Response<Power>) {
                    if(response.body().toString().isNotEmpty()){
                        Toast.makeText(applicationContext, "전원: ${response.body()?.power.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Power>, t: Throwable) {
                    Log.d("Power", "Error")
                }
            })
        }

        binding.refresh.setOnClickListener{
            loadAPIData()
        }
    }

    private fun setAdapter(deviceList: ArrayList<Sensing>) {
        val mAdapter = RetroViewAdapter(deviceList, applicationContext)
        binding.deviceRv.adapter = mAdapter
        binding.deviceRv.layoutManager = LinearLayoutManager(this)
    }

    private fun loadAPIData(){
        val prefs = PreferencesUtil(applicationContext)
        val devID = prefs.getString("ConnectedID", "ID Error")
        val api = APIS.create()
        api.searchApp(Device(deviceId = devID)).enqueue(object : Callback<ArrayList<Sensing>>{
            override fun onResponse(
                call: Call<ArrayList<Sensing>>,
                response: Response<ArrayList<Sensing>>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let{
                        setAdapter(it)
                        Toast.makeText(applicationContext, "값 호출 성공", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Sensing>>, t: Throwable) {
                t.message?.let { Log.d("API Error", it) }
            }
        })
    }
}