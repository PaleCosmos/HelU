package com.example.halilarm

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_u_chat.*
import kotlinx.android.synthetic.main.toastborder.view.*

class UchatActivity:AppCompatActivity() {
    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        updateStatusBarColor("#E43F3F")
        setContentView(R.layout.activity_u_chat)
        var myId = 0
        var myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        var myName = "Michael"
        var yourId = 1
        var yourIcon = BitmapFactory.decodeResource(resources, R.drawable.face_1)
        var yourName = "Emily"

        val me = ChatUser(myId, myName, myIcon)
        val you = ChatUser(yourId, yourName, yourIcon)

        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.primary));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.primary_darker));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.BLACK);
        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setAutoHidingKeyboard(true) //


        mChatView.setOnClickSendButtonListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var message = Message.Builder()
                    .setUser(me)
                    .setRight(true)
                    .setText(mChatView.inputText)
                    .hideIcon(true)
                    .build()

                mChatView.inputText=""

                Handler().post(object : Runnable {
                    override fun run() {
                        mChatView.receive(message)
                    }
                })
            }
        }
        )
    }
    fun updateStatusBarColor(color: String) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
   if (resultCode == 88) {
            finish()
        }
    }
    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000L) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(applicationContext, "나갈거야?", Toast.LENGTH_SHORT).show()
            return
        } else {
            val back = Intent(this,BackKeyPress::class.java)
            back.putExtra("code",0)
            startActivityForResult(back,1)
        }
    }
}