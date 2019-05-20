package com.example.halilarm

import android.content.Context
import com.firebase.jobdispatcher.*

class JobSchedulerStart {
    private val JOB_ID = 1111
    fun start(context: Context) {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
        val myJob = dispatcher.newJobBuilder()
            .setService(NotificationJobFireBaseService::class.java)
            .setTag("TSLetterNotification")        // 태그 등록
            .setRecurring(false) //재활용
            .setLifetime(Lifetime.FOREVER) //다시켜도 작동을 시킬껀지?
            .setTrigger(Trigger.executionWindow(0, 1)) //트리거 시간
            .setReplaceCurrent(true)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .build()
        dispatcher.mustSchedule(myJob)
    }
}