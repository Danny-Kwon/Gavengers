package com.example.gavengers.alarm

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gavengers.activity.DBViewActivity
import com.example.gavengers.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, DBViewActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(i)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(context!!, "mychannel01").apply{
            setSmallIcon(R.drawable.my_project_1)
            setContentTitle("우리가족지킴이 알람!")
            setAutoCancel(true)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(resultPendingIntent)
            setFullScreenIntent(resultPendingIntent, true)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0,builder.build())
    }
}