package com.pale_cosmos.helu

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.github.bassaer.chatmessageview.model.ChatUser
import com.google.firebase.database.*

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.activity_socket_receive.*
import kotlinx.android.synthetic.main.activity_u_chat.*
import kotlinx.android.synthetic.main.toastborder.view.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket


class SocketReceiveDialog : AppCompatActivity() {
    lateinit var socket: Socket
    val socketAddress = InetSocketAddress("219.248.6.32", 7654)
    lateinit var Dos: DataOutputStream
    lateinit var Dis: DataInputStream
    //    lateinit var storage: FirebaseStorage
//    lateinit var stoRef: StorageReference
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var key: String
    lateinit var nicknames: String
    lateinit var myuniv: String
    lateinit var mydepart: String
    lateinit var gender: String
    lateinit var phone: String
    lateinit var univ: String
    lateinit var depart: String
    lateinit var wantgenderString: String
    lateinit var myInfo: UserInfo
    lateinit var yournickname: String
    lateinit var yourkey: String
    lateinit var yourphone: String
    lateinit var yourInfo: UchatInfo
    var icon: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_socket_receive)

        loader.setBackgroundColor(Color.TRANSPARENT)

        setValue()
        AsyncSocketService().execute("")
    }

    private fun setValue() {
//        storage = FirebaseStorage.getInstance("gs://palecosmos-helu.appspot.com/")
        database = FirebaseDatabase.getInstance()

        myInfo = intent.getSerializableExtra("USERINFO") as UserInfo
        key = intent.getStringExtra("key")
        nicknames = myInfo.nickname!!
        myuniv = myInfo.university!!
        mydepart = myInfo.department!!
        if (myInfo.gender!!) gender = "true"
        else gender = "false"
        phone = myInfo.phone!!
        univ = intent.getStringExtra("univ")
        depart = intent.getStringExtra("depart")
        wantgenderString = intent.getStringExtra("wantgender")

    }

    override fun onDestroy() {
        super.onDestroy()
        if (!socket.isConnected) socket.close()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_OUTSIDE) {

        }

        return false
    }

    inner class AsyncSocketService : AsyncTask<String?, Void, String?>() {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg params: String?): String? {
            var resulting = "failed"
            try {
                socket = Socket() // lateinit

                socket.soTimeout = 300000
                socket.connect(socketAddress, 4000)
                Dos = DataOutputStream(socket.getOutputStream())
                Dis = DataInputStream(socket.getInputStream())
            } catch (e: Exception) {
                return "failedBeforeSocketOpen"
            }

            if (read().split(":")[1].equals("INFORMATION", ignoreCase = true)) {
                write("PROVIDE:INFORMATION:$key,$nicknames,$myuniv,$mydepart,$gender,$phone")
            } else return "failed"
            if (read().split(":")[1].equals("MATCHINGDATA", ignoreCase = true)) {
                write("PROVIDE:MATCHINGDATA:$univ,$depart,$wantgenderString")
            } else return "failed"

            var matchingDataSet = read()
            Log.d("matchingDataSet", matchingDataSet)
            var dataSetSplited = matchingDataSet.split(":")
            if (dataSetSplited[1] == "MATCHINGDATASET") {
                var data = dataSetSplited[2].split(",") // key,nickname,phone
                yourkey = data[0]
                yournickname = data[1]
                yourphone = data[2]
                resulting = "success" // 매칭성공
                Log.d("matchingDataSet", resulting)
            } else return "failed"

            return resulting
        }

        override fun onPostExecute(result: String?) {
            if (!socket.isClosed) socket.close()
            Log.d("matchingDataSet", result)
            when (result) {
                "success" -> {
                    yourInfo = UchatInfo()
                    yourInfo.setInfos(yourkey, yournickname, yourphone, univ, depart, wantgenderString)
//                    stoRef = storage.reference.child("profile").child("$yourkey.png")

                    Log.d("matchingDataSet", yourInfo.key)

                    databaseReference = database.reference.child("users").child(yourkey).child("photo")
                    databaseReference.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                         icon =myUtil.stringToBitmap(p0.getValue(String::class.java)!!)
                            var intd = Intent()
                                intd.putExtra("yourInfo", yourInfo)
                                intd.putExtra("icon", icon)
                                setResult(8080, intd)
                                (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(400)
                                finish()
                        }
                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })


//                    GlideApp.with(applicationContext).asBitmap().load(stoRef)
//                        .override(100,100)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .into(object : SimpleTarget<Bitmap>() {
//                            override fun onResourceReady(
//                                resource: Bitmap,
//                                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                            ) {
//
//                                Log.d("matchingDataSet", "profile/$yourkey.png")
//                                icon = resource
//
//                            }
//                        })
                }

                "failed" -> {
                    setResult(8081)
                    finish()
                }
                "failedBeforeSocketOpen" -> {
                    setResult(8082)
                    finish()
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
                socket.close()
            }
            return msg
        }


    }

    override fun onBackPressed() {
        return
    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}
