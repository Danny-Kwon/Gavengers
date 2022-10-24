package com.example.gavengers.activity

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gavengers.R
import com.example.gavengers.alarm.AlarmReceiver
import com.example.gavengers.databinding.ActivitySecondBinding
import com.example.gavengers.network.*
import com.example.gavengers.sharedpreferences.PreferencesUtil
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SecondActivity: AppCompatActivity() {

    private lateinit var devID : String

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondBinding.inflate(layoutInflater)
        val prefs = PreferencesUtil(applicationContext)
        devID = prefs.getString("ConnectedID", "0")
        setContentView(binding.root)

        when(prefs.getString("Turn", "Off")){
            "On" -> {
                binding.viewHour.text = prefs.getInt("hour", 12).toString()
                binding.viewMinute.text = prefs.getInt("minute", 30).toString()
            }
            "Off" -> {
                Toast.makeText(this, "현재 설정된 알람 없음.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.clock.setOnClickListener{ // 큰 시계 뷰 클릭시 시간 고를 수 있음
            showTimeSettingPopup()
        }

        val settingButton = binding.setting // 알람 설정버튼, 고른 시간으로 알람 진행
        settingButton.setOnClickListener {
            setAlarm(prefs.getInt("hour", 12), prefs.getInt("minute", 30))
            prefs.setString("Turn", "On")
            binding.viewHour.text = prefs.getInt("hour", 12).toString()
            binding.viewMinute.text = prefs.getInt("minute", 30).toString()
        }

        val setOffButton = binding.setOff // 알람 끄기 버튼
        setOffButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            with(builder){
                setTitle("알람을 정말 취소하시겠습니까?")
                setPositiveButton("확인"){ _: DialogInterface, _: Int ->
                    prefs.setString("Turn", "Off")
                    cancelAlarm()
                }
                setNegativeButton("취소"){_: DialogInterface, _: Int ->
                    Toast.makeText(context, "알람 취소하지 않음", Toast.LENGTH_SHORT).show()
                }
                show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAlarm(hour : Int, minute: Int){
        createNotificationChannel()
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        val monthNow = Calendar.getInstance().get(Calendar.MONTH)
        val dateNow = Calendar.getInstance().get(Calendar.DATE)
        val hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minuteNow = Calendar.getInstance().get(Calendar.MINUTE)
        val time = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            if((hourNow >= hour) && (minuteNow >= minute))
            {
                if((monthNow == 2) && (dateNow == 28)){
                    set(Calendar.MONTH, 3)
                    set(Calendar.DATE, 1)
                }else if((monthNow == 12) && (dateNow == 31)){
                    set(Calendar.YEAR, (yearNow + 1))
                    set(Calendar.MONTH, 1)
                    set(Calendar.DATE, 1)
                }
                else if(((monthNow == 1)||(monthNow == 3)||(monthNow == 5)||(monthNow == 7)||(monthNow == 8)||(monthNow == 10))&&(dateNow == 31)){
                    set(Calendar.MONTH, (monthNow + 1))
                    set(Calendar.DATE, 1)
                }else if(Calendar.DATE == 30){
                    set(Calendar.MONTH, (monthNow + 1))
                    set(Calendar.DATE, 1)
                }else{
                    set(Calendar.DATE, (dateNow + 1))
                }
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }else{
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
        }
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,
            PendingIntent.FLAG_IMMUTABLE)
        Toast.makeText(this, "$hour 시 $minute 분 에 알람 설정됨", Toast.LENGTH_SHORT).show()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,time.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)

        Toast.makeText(this,"알람 해제됨", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showTimeSettingPopup(){
        val prefs = PreferencesUtil(applicationContext)
        var set = 0
        var set2 = 0
        val build = AlertDialog.Builder(this).create()
        val edialog : LayoutInflater = LayoutInflater.from(applicationContext)
        val mView : View = edialog.inflate(R.layout.popup_settime, null)
        val hour: NumberPicker = mView.findViewById(R.id.numberPicker_hour)
        val min : NumberPicker = mView.findViewById(R.id.numberPicker_min)
        val confirm : Button = mView.findViewById(R.id.btn_settime_ok)
        val cancel : Button = mView.findViewById(R.id.btn_settime_no)
        with(hour){
            value = prefs.getInt("hour", 0)
            minValue = 0
            maxValue = 23
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener{ _, _, i2 ->
                set = i2
                prefs.setInt("hour", set)
            }
        }
        with(min){
            value = prefs.getInt("minute", 0)
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            setOnValueChangedListener{ _, _, i2 ->
                set2 = i2
                prefs.setInt("minute", set2)
            }
        }
        confirm.setOnClickListener{
            Toast.makeText(this, "시간 선택 후 ‘알람 켜기’ 버튼을 누르십시오.", Toast.LENGTH_SHORT).show()
            build.dismiss()
        }
        cancel.setOnClickListener{
            Toast.makeText(this, "알람 설정하지 않음", Toast.LENGTH_SHORT).show()
            build.dismiss()
        }
        with(build){
            setView(mView)
            create()
            show()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mychannel01"
            val name = "channel01"
            val channelDescription = "alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH // set importance
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = channelDescription
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}