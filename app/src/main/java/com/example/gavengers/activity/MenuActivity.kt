package com.example.gavengers.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gavengers.databinding.ActivityMenuBinding
import com.example.gavengers.sharedpreferences.PreferencesUtil
import com.kakao.sdk.user.UserApiClient

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs = PreferencesUtil(applicationContext)
        val devID = prefs.getString("ConnectedID", "0")
        val nickname = binding.nickname // 상단 닉네임 표시 뷰
        UserApiClient.instance.me { user, _ ->
            nickname.text = "기기 번호 : $devID"
        }

        binding.enterValue.setOnClickListener {
            val intentView = Intent(this, DBViewActivity::class.java)
            startActivity(intentView)
        }

        binding.setDevId.setOnClickListener{
            val intent = Intent(this, DeviceActivity::class.java)
            startActivity(intent)
        }

        binding.enterAlarm.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        binding.enterMy.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}