package com.example.gavengers.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.gavengers.adapter.DeviceAdapter
import com.example.gavengers.database.DeviceData
import com.example.gavengers.databinding.ActivityDeviceBinding
import com.example.gavengers.databinding.AlertdialogEdittextBinding
import com.example.gavengers.network.*
import com.example.gavengers.sharedpreferences.PreferencesUtil
import com.example.gavengers.viewmodel.DeviceViewModel
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeviceActivity : AppCompatActivity() {
    private val api = APIS.create()
    lateinit var recyclerViewAdapter: DeviceAdapter
    lateinit var viewModel: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = PreferencesUtil(applicationContext)

        var nickname: String? = null
        UserApiClient.instance.me{user, _ ->
            if (user != null) {
                nickname = user.kakaoAccount?.profile?.nickname.toString()
                prefs.setString("name", nickname!!)
            }
        }
        var tok : String
        //카카오 토큰값 저장 변수
        UserApiClient.instance.accessTokenInfo{tokenInfo, error ->
            if(error != null){
                Log.d("카카오 토큰", "에러 발생")
            }else if(tokenInfo != null){
                tok = tokenInfo.id.toString()
                Log.d("카카오 토큰", "$tok")
                prefs.setString("tok", tok)
            }
        }
        val tok1: String = prefs.getString("tok", "Token Error")

        val data1 = User(userPk = prefs.getString("tok", "Token1"))
        api.registerUser(data1).enqueue(object : Callback<OkSign> {
            override fun onResponse(call: Call<OkSign>, response: Response<OkSign>) {
                Log.d("log", response.toString())
                Log.d("log", response.body()?.okSign.toString())
                if (response.body().toString().isNotEmpty())
                    Log.d("log", response.toString())
            }
            override fun onFailure(call: Call<OkSign>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("registerUser", "Fail")
            }
        })

        binding.recycler.apply{
            layoutManager = LinearLayoutManager(this@DeviceActivity)
            recyclerViewAdapter = DeviceAdapter()
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }
        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]
        viewModel.getAllIdsObservers().observe(this) {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        }

        viewModel.getAllIds()

        binding.deviceAccess.setOnClickListener {
            //기기 접속 버튼
            val id: String = binding.deviceInput.text.toString()
            prefs.setString("ConnectedID", id)
            Toast.makeText(applicationContext, "$id 기기로 접속합니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, SecondActivity::class.java)
            startActivity(intent)
        }

        binding.addDv.setOnClickListener {
            //기기 추가 팝업
            val builder = AlertDialog.Builder(this)
            val builderItem = AlertdialogEdittextBinding.inflate(layoutInflater)
            val edittext = builderItem.editText
            with(builder){
                setTitle("기기 추가")
                setMessage("기기 ID값을 입력하세요.")
                setView(builderItem.root)
                setPositiveButton("확인"){ _:DialogInterface, _: Int ->
                    if(edittext.text!=null) {
                        prefs.setString("ID", edittext.text.toString())
                        deviceToDB(tok1, edittext.text.toString())
                    }
                }
                setNegativeButton("취소"){ _:DialogInterface, _: Int ->
                    Toast.makeText(context, "기기 추가하지 않음", Toast.LENGTH_SHORT).show()
                }
                show()
            }
        }

        binding.deleteDv.setOnClickListener {
            //기기 제거 팝업
            val builder = AlertDialog.Builder(this)
            val builderItem = AlertdialogEdittextBinding.inflate(layoutInflater)
            val edittext = builderItem.editText
            with(builder){
                setTitle("기기 제거")
                setMessage("제거할 기기의 ID값을 입력하세요.")
                setView(builderItem.root)
                setPositiveButton("확인"){ _:DialogInterface, _: Int ->
                    if(edittext.text!=null){
                        Toast.makeText(context, "${edittext.text}기기 삭제됨", Toast.LENGTH_SHORT).show()
                        removeDevice(tok1, edittext.text.toString())
                        nickname?.let { it1 ->
                            DeviceData(edittext.text.toString(),
                                it1
                            )
                        }?.let { it2 -> viewModel.deleteDevice(it2) }
                    }
                }
                setNegativeButton("취소"){ _:DialogInterface, _:Int ->
                    Toast.makeText(context, "기기 제거하지 않음", Toast.LENGTH_SHORT).show()
                }
                show()
            }
        }


    }

    private fun deviceToDB(tok: String, ID: String){
        val data = UserDevice(userPk = tok, deviceId = ID)
        val prefs = PreferencesUtil(applicationContext)
        api.registerUserdevice(data).enqueue(object : Callback<OkSign> {
            override fun onResponse(call: Call<OkSign>, response: Response<OkSign>) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())
                when(response.body()?.okSign.toString()){
                    "Ok" -> {
                        Log.d("log", response.body()?.okSign.toString())
                        Toast.makeText(applicationContext, "기기가 등록 되었습니다.", Toast.LENGTH_SHORT).show()
                        DeviceData(prefs.getString("ID", "1"),
                            prefs.getString("name", "Name")
                        ).let { it2 -> viewModel.insertDevice(it2)}
                    }
                    "deviceDuplicate" -> Toast.makeText(applicationContext, "이미 저장된 기기입니다.", Toast.LENGTH_SHORT).show()
                    "deviceNotFound" -> Toast.makeText(applicationContext, "서버에 저장되지 않은 기기입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<OkSign>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("registerUserDevice", "fail")
            }
        })
    }

    private fun removeDevice(tok: String, ID: String){
        val data = UserDevice(userPk = tok, deviceId = ID)
        api.deleteUserOption(data).enqueue(object : Callback<OkSign>{
            override fun onResponse(call: Call<OkSign>, response: Response<OkSign>){
                Log.d("log", response.toString())
                Log.d("log", response.toString())
                if (response.body()?.okSign.toString().isNotEmpty())
                    Log.d("log", response.toString())
            }
            override fun onFailure(call: Call<OkSign>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("deleteUserOption", "fail")
            }
        })
    }
}