package com.pale_cosmos.helu
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * 노티피케이션 채널 생성하기 안드로이드 버전 오레오 이상부터 필요
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.notification_channel_id)
            val channelName = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}