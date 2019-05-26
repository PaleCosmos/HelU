package com.pale_cosmos.helu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat



class AlarmBroadcastReceiver:BroadcastReceiver() {
    private val NOTICATION_ID = 222

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmBroadcastReceiver", "onReceive")

        var builder = NotificationCompat.Builder(context!!, context.getString(R.string.notification_channel_id))
            .setSmallIcon(R.mipmap.ic_launcher) //알람 아이콘
            .setContentTitle("알림")  //알람 제목
            .setContentText("넌 멍청이야") //알람 내용
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //알람 중요도

        var notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTICATION_ID, builder.build()) //알람 생성
    }

}