package com.pale_cosmos.helu
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.firebase.jobdispatcher.JobParameters

class NotificationJobFireBaseService : com.firebase.jobdispatcher.JobService() {

    private val time = 3000
    override fun onStartJob(job: JobParameters?): Boolean {
        Log.d("NotificationJobService", "onStartJob")
        var manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(this, AlarmBroadcastReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(this, 111, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        /**
         * Intent 플래그
         * FLAG_ONE_SHOT : 한번만 사용하고 다음에 이 PendingIntent가 불려지면 Fail을 함
         * FLAG_NO_CREATE : PendingIntent를 생성하지 않음. PendingIntent가 실행중인것을 체크를 함
         * FLAG_CANCEL_CURRENT : 실행중인 PendingIntent가 있다면 기존 인텐트를 취소하고 새로만듬
         * FLAG_UPDATE_CURRENT : 실행중인 PendingIntent가 있다면  Extra Data만 교체함
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + time,
                pendingIntent
            ) //10초뒤 알람
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent)
        }

        /**
         * AlarmType
         * RTC_WAKEUP : 대기모드에서도 알람이 작동함을 의미함
         * RTC : 대기모드에선 알람을 작동안함
         */

        return false // Answers the question: "Is there still work going on?"  }
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return false
    }
}