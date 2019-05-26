package com.pale_cosmos.helu


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_u_chat.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class UchatActivity : AppCompatActivity(), View.OnClickListener {
    var switch = true
    lateinit var nicknames: String
    lateinit var key: String
    lateinit var univ: String
    lateinit var depart: String
    lateinit var gender: String
    lateinit var phone: String
    var socket: Socket? = null
    lateinit var Dos: DataOutputStream
    lateinit var Dis: DataInputStream
    lateinit var yourkey: String
    lateinit var yournickname: String
    lateinit var yourphone: String
    lateinit var receiver: Receiver
    lateinit var myuniv: String
    lateinit var mydepart: String
    lateinit var me: ChatUser
    lateinit var you: ChatUser
    lateinit var wantgenderString: String
    val socketAddress = InetSocketAddress("219.248.6.32", 7654)
    var wantgender = true
    var flagTT = true
    var myId: Int = 0
    lateinit var myIcon: Bitmap
    var yourId = 1
    var yourIcon: Bitmap? = null

    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        updateStatusBarColor("#E43F3F")
        setContentView(R.layout.activity_u_chat)
        initializationChatView()
        initialization()

    }

    private fun initialization() {

        setValue()

        AsyncSocketService().execute("")


    }

    private fun setValue() {
        myId = 0
        myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        yourId = 1

        wantgender = intent.getBooleanExtra("wantgender", true)
        wantgenderString = if (wantgender) {
            "true"
        } else {

            "false"
        }
        nicknames = intent.getStringExtra("nickname")
        key = intent.getStringExtra("key")
        univ = intent.getStringExtra("univ")
        depart = intent.getStringExtra("depart")
        gender = intent.getStringExtra("gender")
        phone = intent.getStringExtra("phone")
        mydepart = intent.getStringExtra("mydepart")
        myuniv = intent.getStringExtra("myuniv")
        me = ChatUser(0, nicknames, myIcon)
    }

    private fun initializationChatView() {
        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.primary));
        mChatView.setLeftBubbleColor(R.color.primary);
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.primary_darker));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.WHITE)
        mChatView.setMessageStatusTextColor(Color.BLACK)

        mChatView.setUsernameTextColor(Color.WHITE);
        mChatView.setSendTimeTextColor(Color.WHITE);
        mChatView.setDateSeparatorColor(Color.WHITE);
        mChatView.inputTextColor = R.color.primary_darker

        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);
        mChatView.setAutoHidingKeyboard(true) //


        mChatView.setOnClickSendButtonListener(this)
        mChatView.isEnabled = false

    }

    fun updateStatusBarColor(color: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }

    override fun onClick(v: View?) {

        var text = mChatView.inputText

        if (!text.isBlank()) {
            var message = Message.Builder()
                .setUser(me)
                .setRight(true)
                .setText(text)
                .hideIcon(true)
                .build()


            Send(text).start()

            mChatView.inputText = ""
            Handler().post {
                mChatView.receive(message)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 88) {
            Send("aAbBdDefawefaewf").start()
            flagTT = false
            switch = false
            finish()
            socket?.close()
        }
        if (resultCode == 32) {
            flagTT = false
            switch = false
            finish()
            socket?.close()
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

    inner class SocketManager : Thread() {
        override fun run() {
            while (!socket?.isClosed!!) {
                sleep(5000)
            }
            if (flagTT)
                startActivityForResult(Intent(applicationContext, ConnectingException::class.java), 1)
        }
    }

    inner class AsyncSocketService : AsyncTask<String?, Void, String?>() {
        private var asyncDialog: ProgressDialog? = null


        fun onStart() { // 안보여줌


        }

        fun onStop() { // 보여줌

        }

        override fun onPreExecute() {
            onStart()
        }

        override fun doInBackground(vararg params: String?): String? {
            var resulting = "failed"
            try {
                socket = Socket()

                socket?.soTimeout = 300000
                socket?.connect(socketAddress, 3000)
                Dos = DataOutputStream(socket?.getOutputStream())
                Dis = DataInputStream(socket?.getInputStream())
            } catch (e: Exception) {
                socket?.close()
                return "failedBeforeSocketOpen"
            }
            SocketManager().start()
            if (read().split(":")[1].equals("INFORMATION", ignoreCase = true)) {
                write("PROVIDE:INFORMATION:$key,$nicknames,$myuniv,$mydepart,$gender,$phone\"")
            } else return "failed"

            if (read().split(":")[1].equals("MATCHINGDATA", ignoreCase = true)) {
                write("PROVIDE:MATCHINGDATA:$univ,$depart,$wantgenderString")
            } else return "failed"

            var matchingDataSet = read()
            var dataSetSplited = matchingDataSet.split(":")
            if (dataSetSplited[1] == "MATCHINGDATASET") {
                var data = dataSetSplited[2].split(",") // key,nickname,phone
                yourkey = data[0]
                yournickname = data[1]
                yourphone = data[2]
                resulting = "success" // 매칭성공
            } else return "failed"

            return resulting
        }

        override fun onPostExecute(result: String?) {
            Log.d("asynctask", result)
            when (result) {
                "success" -> {
                    var fs = FirebaseStorage.getInstance()
                    var imagesRef = fs.reference.child("profile/$yourkey.png")


                    Glide.with(applicationContext).asBitmap().load(imagesRef)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                yourIcon = resource
                                you = ChatUser(1, yournickname, yourIcon!!)
                            }
                        })
                    onStop()
                    receiver = Receiver()
                    receiver.start()
                    mChatView.isEnabled = true
                }

                "failed" -> {
                    socket?.close()
                }
                "failedBeforeSocketOpen" -> {
                    startActivityForResult(Intent(applicationContext, ConnectingException::class.java), 1)
                }

            }
            return

        }


        private fun write(msg: String) {
            Dos.writeUTF(msg)
            Dos.flush()
        }

        private fun read(): String {
            var msg: String = "null"
            try {
                msg = Dis.readUTF()
            } catch (e: Exception) {
                msg = "null:A:A"
                socket?.close()
            }
            return msg
        }


    }

    inner class Send(msg: String) : Thread() {
        var message = "MESSAGE:$nicknames:$msg"
        override fun run() {
            try {
                Dos.writeUTF(message)
                Dos.flush()
            } catch (e: Exception) {
                socket?.close()
            }
        }

    }

    inner class Receiver : Thread() {
        override fun run() {

            while (switch) {
                var msg = read()

                var tokens = msg.split(":")

                when (tokens[0]) {

                    "ACTION" -> {
                        when (tokens[1]) {
                            "EXIT" -> {
                                socket?.close()

                            }
                        }
                    }
                    "MESSAGE" -> {
                        if(tokens[2]=="aAbBdDefawefaewf")
                        {
                            runOnUiThread {
                                startActivityForResult(Intent(applicationContext,ConnectingException::class.java),2)
                            }
                        }
                        else
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

        private fun write(msg: String) {
            try {
                Dos.writeUTF(msg)
                Dos.flush()
            } catch (e: Exception) {
                switch = false
                socket?.close()
            }
        }

        private fun read(): String {
            var g = "n:n:n"
            try {
                var g = Dis.readUTF()
                return g
            } catch (e: Exception) {
                switch = false
                socket?.close()
                return "a:a:a"
            }
        }
    }
}