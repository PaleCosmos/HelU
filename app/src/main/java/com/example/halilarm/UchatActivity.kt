package com.example.halilarm

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import kotlinx.android.synthetic.main.activity_u_chat.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket


class UchatActivity : AppCompatActivity() {
    lateinit var nicknames: String
    lateinit var key: String
    lateinit var univ: String
    lateinit var depart: String
    lateinit var gender: String
    lateinit var phone: String
    lateinit var socket: Socket
    lateinit var Dos: DataOutputStream
    lateinit var Dis: DataInputStream
    lateinit var yourkey: String
    lateinit var yournickname: String
    lateinit var yourphone: String


    lateinit var me: ChatUser
    lateinit var you: ChatUser

    var myId: Int = 0
    lateinit var myIcon: Bitmap
    var yourId = 1
    lateinit var yourIcon: Bitmap

    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        updateStatusBarColor("#E43F3F")
        setContentView(R.layout.activity_u_chat)
        myId = 0
        myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        yourId = 1
        yourIcon = BitmapFactory.decodeResource(resources, R.drawable.face_1)


        nicknames = intent.getStringExtra("nickname")
        key = intent.getStringExtra("key")
        univ = intent.getStringExtra("univ")
        depart = intent.getStringExtra("depart")
        gender = intent.getStringExtra("gender")
        phone = intent.getStringExtra("phone")
        me = ChatUser(0, nicknames, myIcon)

                Receiver().start()

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



                Runnable {
                    write("MESSAGE:$nicknames:${mChatView.inputText}")
                }
                mChatView.inputText = ""
                Handler().post {
                    mChatView.receive(message)
                }
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
            val back = Intent(this, BackKeyPress::class.java)
            back.putExtra("code", 0)
            startActivityForResult(back, 1)
        }
    }

    inner class Receiver : Thread() {

        override fun run() {
            socket = Socket("219.248.6.32", 7654)
            Dos = DataOutputStream(socket.getOutputStream())
            Dis = DataInputStream(socket.getInputStream())
            Log.d("anggimotti", "tlqkf")
            while (true) {
                var msg = read()
                Log.d("anggimotti", msg)
                var tokens = msg.split(":")

                when (tokens[0]) {
                    "REQUEST" -> {
                        when (tokens[1]) {
                            "INFORMATION" -> write("PROVIDE:INFORMATION:$key,$nicknames,가천대학교,소프트웨어학과,$gender,$phone")
                            "MATCHINGDATA" -> write("PROVIDE:MATCHINGDATA:$univ,$depart,true")
                        }
                    }
                    "PROVIDE" -> {
                        when (tokens[1]) {
                            "MATCHINGDATASET" -> {
                                var data = tokens[2].split(",")
                                yourkey = data[0]
                                yournickname = data[1]
                                yourphone = data[2]
                                write("ACTION:NULL:NULL")

                                you = ChatUser(1, yournickname, yourIcon)
                            }
                        }
                    }
                    "ACTION" -> {
                        when (tokens[1]) {

                        }
                    }
                    "MESSAGE" -> {
                        runOnUiThread {
                            var textMessage = tokens[2]
                            var message = Message.Builder()
                                .setUser(you)
                                .setRight(false)
                                .setText(textMessage)
                                .hideIcon(false)
                                .build()
                            mChatView.receive(message)
                        }
                    }
                    else -> {
                    }
                }
            }
        }

        fun write(msg: String) {
            Dos.writeUTF(msg)
            Dos.flush()
        }

        fun read(): String {
            var g = Dis.readUTF()
            return g
        }
    }

    fun write(msg: String) {
        Dos.writeUTF(msg)
        Dos.flush()
    }
}