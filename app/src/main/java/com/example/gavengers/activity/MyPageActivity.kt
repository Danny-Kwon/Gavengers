package com.example.gavengers.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gavengers.databinding.ActivityMyPageBinding
import com.example.gavengers.network.APIS
import com.example.gavengers.network.OkSign
import com.example.gavengers.network.User
import com.example.gavengers.sharedpreferences.PreferencesUtil
import com.example.gavengers.viewmodel.DeviceViewModel
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageActivity : AppCompatActivity() {
    private val api = APIS.create()
    lateinit var viewModel: DeviceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = PreferencesUtil(applicationContext)
        val tok: String = prefs.getString("tok", "Token Error")

        val kakaoLogoutButton = binding.kakaoLogoutButton// 로그아웃 버튼
        kakaoLogoutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        val kakaoUnlinkButton = binding.kakaoUnlinkButton // 회원 탈퇴 버튼

        kakaoUnlinkButton.setOnClickListener {
            val data = User(userPk = tok)
            viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]
            viewModel.deleteAll()
            api.deleteUser(data).enqueue(object : Callback<OkSign> {
                override fun onResponse(call: Call<OkSign>, response: Response<OkSign>) {
                    Log.d("deleteUser", "fail")
                    if (response.body()?.okSign.toString().isNotEmpty())
                        Log.d("log", response.toString())
                }
                override fun onFailure(call: Call<OkSign>, t: Throwable) {
                    Log.d("deleteUser", "fail")
                }
            })
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }
        }

        binding.kakaoId.text = prefs.getString("name", "error")
    }
}