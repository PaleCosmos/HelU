package com.pale_cosmos.helu

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import androidx.fragment.app.Fragment
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import com.google.firebase.database.*
import com.pale_cosmos.helu.util.myUtil
import org.w3c.dom.Text
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.net.Socket

class Fragment4 : Fragment() {
    lateinit var mtalk: ChatView
    lateinit var ref: DatabaseReference
    lateinit var me: ChatUser
    lateinit var myNick: String
    lateinit var myKey: String
    lateinit var myProfile: String
    lateinit var un: AppCompatTextView
    lateinit var de: AppCompatTextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment4, container, false) as ViewGroup
        un = view.findViewById(R.id.universText)
        de = view.findViewById(R.id.departText)
        un.text = myUtil.myInfo?.university
        myNick = myUtil.myInfo?.nickname!!
        myKey = myUtil.myKey
        myProfile = myUtil.myProfile!!
        de.text = myUtil.myInfo?.department
        mtalk = view.findViewById(R.id.quatalk)
        ref = FirebaseDatabase.getInstance().reference.child("quatalk").child(myUtil.myInfo?.university!!)
            .child(myUtil.myInfo?.department!!)
        me = ChatUser(0, myNick!!, myUtil.stringToBitmap(myProfile))
        initializationChatView()
        addChildListender(ref)
        return view
    }


    private fun initializationChatView() {
        mtalk.setRightBubbleColor(ContextCompat.getColor(activity?.applicationContext!!, R.color.primary))
        mtalk.setLeftBubbleColor(R.color.primary)
        mtalk.setBackgroundColor(ContextCompat.getColor(activity?.applicationContext!!, R.color.blueGray500))
        mtalk.setSendButtonColor(ContextCompat.getColor(activity?.applicationContext!!, R.color.primary_darker))
        mtalk.setSendIcon(R.drawable.ic_action_send)
        mtalk.setRightMessageTextColor(Color.WHITE)
        mtalk.setLeftMessageTextColor(Color.WHITE)
        mtalk.setMessageStatusTextColor(Color.BLACK)
        mtalk.setUsernameTextColor(Color.WHITE)
        mtalk.setSendTimeTextColor(Color.WHITE)
        mtalk.setDateSeparatorColor(Color.WHITE)
        mtalk.inputTextColor = R.color.primary_darker
        mtalk.setInputTextHint("new message...")
        mtalk.setMessageMarginTop(5)
        mtalk.setMessageMarginBottom(5)
        mtalk.setAutoHidingKeyboard(true)
        mtalk.setOnClickSendButtonListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var text = mtalk.inputText

                if (!text.isBlank()) {
                    Log.d("Fragment4", "send : $myNick")
                    val msg = ChatValue()
                    msg.type = "message"
                    msg.key = myKey
                    msg.nickname = myNick
                    msg.message = text
                    msg.profile = myProfile!!
                    msg.yourkey = "nullable"
                    msg.yourprofile = "nullable"
                    msg.yournickname = "nullable"
                    msg.photo = ""
                    ref.push().setValue(msg)

                    mtalk.inputText = ""
                }
            }
        })
        mtalk.isEnabled = false
        mtalk.setOnClickOptionButtonListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var tents = Intent(activity, ProfileActivity::class.java)
                tents.putExtra("by", 5)
                startActivityForResult(tents, 37)
            }
        })
        mtalk.setOptionButtonColor(R.color.primary_darker)
    }

    private fun addChildListender(ref: DatabaseReference) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(ChatValue::class.java)
                if (data?.type == "test") return
                var user: ChatUser? = null
                var flag = true
                if (data?.key == myKey) {
                    user = me
                    flag = true

                } else {
//                    user = you
                    flag = false

                }
                val setting = ChatUser(0, data?.nickname!!, myUtil.stringToBitmap(data.profile))
                Log.d("Fragment4", "receive : ${data?.nickname}")
                when (data?.type) {

                    "message" -> {

                        var message = Message.Builder()
                            .setUser(setting)
                            .setRight(flag)
                            .setText(data.message!!)
                            .hideIcon(flag)
                            .build()
                        Handler().post {
                            mtalk.receive(message)
//                            fg = false
                        }
                    }
                    "photo" -> {
                        var message = Message.Builder()
                            .setUser(setting)
                            .setRight(flag)
                            .setType(Message.Type.PICTURE)
                            .setPicture(myUtil.stringToBitmap(data?.photo))
                            .setText("")
                            .hideIcon(flag)
                            .build()
                        Handler().post {
                            mtalk.receive(message)
                        }
//                        fg = false
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
        })
    }


    private fun sendImage(map: Bitmap) {
        val msg = ChatValue()
        msg.type = "photo"
        msg.key = myKey
        msg.message = ""
        msg.yourkey = "nullable"
        msg.yourprofile = "nullable"
        msg.yournickname = "nullable"
        msg.nickname = myNick
        msg.photo = myUtil.bitmapToString(map)
        msg.profile = myProfile!!
        ref.push().setValue(msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            175 -> {
                var profileUri = data?.getParcelableExtra("profileUri") as Uri


                var bitg = MediaStore.Images.Media.getBitmap(
                    activity?.contentResolver,
                    profileUri
                )

                sendImage(bitg)//

                var myFile = File(profileUri.path)
                if (myFile.exists()) myFile.delete()

            }

        }
    }
}