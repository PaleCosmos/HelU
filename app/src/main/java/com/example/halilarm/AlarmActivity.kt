package com.example.halilarm

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.alarm.*
import java.util.*

class AlarmActivity : AppCompatActivity(), DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    var mTimePicker: TimePicker? = null
    var mDatePicker: DatePicker? = null
    private var mediaPlayer: MediaPlayer?=null
    var myContext: Context? = null
    var mManager: AlarmManager? = null
    var mCalendar: Calendar? = null
    var mNotification: NotificationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.alarm)
        window.setFeatureDrawableResource(Window.FEATURE_NO_TITLE, android.R.drawable.ic_dialog_alert)
        mNotification = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mCalendar = GregorianCalendar()

        mTimePicker = findViewById(R.id.time_picker)
        mDatePicker = findViewById(R.id.date_picker)
        myContext = this.applicationContext
        var calendar: Calendar = Calendar.getInstance()


        c1?.setOnClickListener {
            var hour: Int? = 0
            var minutes: Int? = 0
            var year: Int? = 0
            var month: Int? = 0
            var day: Int? = 0
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = mTimePicker?.hour
                minutes = mTimePicker?.minute

            } else {
                hour = mTimePicker?.currentHour
                minutes = mTimePicker?.currentMinute

            }
            year = mDatePicker?.year
            month = mDatePicker?.month
            day = mDatePicker?.dayOfMonth

            //JobSchedulerStart.start(this) //잡스케쥴러
            setAlarm()

        }
        c2.setOnClickListener {
           // resetAlarm()
            mediaPlayer = MediaPlayer.create(this,R.raw.matcha)
            mediaPlayer?.start()
        }
        mDatePicker?.init(
            mCalendar?.get(Calendar.YEAR)!!, mCalendar?.get(Calendar.MONTH)!!,
            mCalendar?.get(Calendar.DAY_OF_MONTH)!!, this
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mTimePicker?.hour = mCalendar?.get(Calendar.HOUR_OF_DAY)!!
            mTimePicker?.minute = mCalendar?.get(Calendar.MINUTE)!!
        }else{
            mTimePicker?.currentHour = mCalendar?.get(Calendar.HOUR_OF_DAY)!!
            mTimePicker?.currentMinute = mCalendar?.get(Calendar.MINUTE)!!
        }
        mTimePicker?.setOnTimeChangedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 바깥 클릭하면 안닫히게
        // 레지스터 이벤트 조정 필요
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        finish()
        return
    }

    private fun setAlarm() {
        mManager?.set(AlarmManager.RTC_WAKEUP, mCalendar?.timeInMillis!!, pendingIntent())
        Log.i(
            "HelloAlarm Activity"
            , mCalendar?.time.toString()
        )
    }

    private fun resetAlarm() {
        mManager?.cancel(pendingIntent())
    }

    private fun pendingIntent(): PendingIntent {
        var i = Intent(applicationContext,ringing::class.java)
        return PendingIntent.getActivity(this, 0, i, 0)
    }

    override fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mCalendar?.set(year, monthOfYear, dayOfMonth, time_picker?.hour!!, time_picker?.minute!!)
        } else {
            mCalendar?.set(year, monthOfYear, dayOfMonth, time_picker?.currentHour!!, time_picker?.currentMinute!!)
        }
        Log.i("HelloAlarmActivity", mCalendar?.time.toString())
    }

    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        mCalendar?.set(mDatePicker?.year!!, mDatePicker?.month!!, mDatePicker?.dayOfMonth!!, hourOfDay, minute)
        Log.i("HelloAlarmActivity", mCalendar?.time.toString())
    }

}