package com.example.halilarm

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class Fragment4: Fragment() {
    var key:String?=null
    var nick:String?=null
    var univ:String?=null
    var dep:String?=null
    var gender:String?=null
    var phone:String?=null
    var bt:Button?=null
    var tx:TextView?=null
    var et:EditText?=null
    var hd:Handler?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment4, container, false) as ViewGroup
         bt = view.findViewById<Button>(R.id.btbt)
         tx=view.findViewById<TextView>(R.id.tvtv)
         et=view.findViewById<EditText>(R.id.eet)


        key=arguments?.getString("key")
        nick=arguments?.getString("nickname")
        var genders=arguments?.getBoolean("gender",true)
        if(genders!!.equals(true))gender="true"
        else gender="false"
        phone=arguments?.getString("phone")
        hd= Handler()
        bt?.setOnClickListener {
            chatThread().start()
        }

        return view
    }
    inner class chatThread:Thread(){
        override fun run() {
            val socket= Socket("219.248.6.32",7654)
            val dos = DataOutputStream(socket.getOutputStream())
            val dis =DataInputStream(socket.getInputStream())

            dos.writeUTF("HELLO")
            dos.flush()

            var XX=dis.readUTF()
            hd?.post(Runnable {
               tx?.append("$XX \n")
            })

            var ms = XX.split(":")
            if(ms[0].equals("request",ignoreCase = true)&&ms[1].equals("information",ignoreCase = true)) {
                dos.writeUTF("PROVIDE:INFORMATION:${key},${nick},Gachon,SoftWare,${gender},${phone}")
                dos.flush()
            }

            var YY=dis.readUTF()
            hd?.post(Runnable {
                tx?.append("$YY \n")
            })
            var mg = YY.split(":")
            if(mg[0].equals("request",ignoreCase = true)&&mg[1].equals("matchingdata",ignoreCase = true)){
                dos.writeUTF("PROVIDE:MATCHINGDATA:Gachon,SoftWare,true")
                dos.flush()
            }

        }
    }
}