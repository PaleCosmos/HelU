package com.example.halilarm

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import java.util.*


class Fragment3 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment3, container, false) as ViewGroup
        var myId = 0
        var myIcon = BitmapFactory.decodeResource(resources, R.drawable.face_2)
        var myName = "Michael"
        var yourId = 1
        var yourIcon = BitmapFactory.decodeResource(resources, R.drawable.face_1)
        var yourName = "Emily"

        val me = ChatUser(myId, myName, myIcon)
        val you = ChatUser(yourId, yourName, yourIcon)

        var mChatView = view.findViewById<ChatView>(R.id.chat_view)
        mChatView.setRightBubbleColor(ContextCompat.getColor(view.context, R.color.green500));
        mChatView.setLeftBubbleColor(Color.WHITE);
        mChatView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.blueGray500));
        mChatView.setSendButtonColor(ContextCompat.getColor(view.context, R.color.primary_darker));
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


                Handler().post(object : Runnable {
                    override fun run() {
                        mChatView.receive(message)
                    }
                })
            }
        }
        )
        return view
    }
}