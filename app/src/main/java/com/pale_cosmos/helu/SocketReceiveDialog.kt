package com.pale_cosmos.helu

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.transition.Transition
import com.bumptech.glide.request.target.SimpleTarget

import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_u_chat.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket


class SocketReceiveDialog : AppCompatActivity() {
    lateinit var socket: Socket
    val socketAddress = InetSocketAddress("219.248.6.32", 7654)
    lateinit var Dos: DataOutputStream
    lateinit var Dis: DataInputStream

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_socket_receive)
        setValue()
        AsyncSocketService().execute("")
    }

    private fun setValue() {
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


    inner class AsyncSocketService : AsyncTask<String?, Void, String?>() {

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg params: String?): String? {
            var resulting = "failed"
            try {
                socket = Socket() // lateinit

                socket.soTimeout = 300000
                socket.connect(socketAddress, 3000)
                Dos = DataOutputStream(socket.getOutputStream())
                Dis = DataInputStream(socket.getInputStream())
            } catch (e: Exception) {
                return "failedBeforeSocketOpen"
            }

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

            if(!socket.isClosed)socket.close()
            when (result) {
                "success" -> {
                    yourInfo= UchatInfo()
                    yourInfo.setInfos(yourkey,yournickname,yourphone,univ,depart,wantgenderString)

                    var intd = Intent()
                    intd.putExtra("yourInfo",yourInfo)

                    setResult(8080,intd)
                    finish()
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
                socket?.close()
            }
            return msg
        }


    }

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            super.setRequestedOrientation(requestedOrientation)
        }

    }
}
