package com.pale_cosmos.helu


import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_u_chat.*

class UchatActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var database:FirebaseDatabase
    lateinit var storage:FirebaseStorage
    lateinit var dataRef:DatabaseReference
    lateinit var stoRef:StorageReference
    lateinit var nicknames: String
    lateinit var key: String
    lateinit var univ: String
    lateinit var depart: String
    lateinit var me: ChatUser
    lateinit var you: ChatUser
    lateinit var myInfo: UserInfo
    lateinit var wantgenderString: String
    lateinit var intents: Intent
    lateinit var yourInfo: UchatInfo
    var wantgender = true
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
        initializationChatView()
        initialization()

    }

    private fun initialization() {

        setValue()
        intents = Intent(this@UchatActivity, SocketReceiveDialog::class.java)
        intents.putExtra("USERINFO", myInfo)
        intents.putExtra("key", key)
        intents.putExtra("univ", univ)
        intents.putExtra("depart", depart)
        intents.putExtra("wantgender", wantgenderString)
        startActivityForResult(intents, 1)
        database= FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()


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
        myInfo = intent.getSerializableExtra("USERINFO") as UserInfo
        key = intent.getStringExtra("key")
        univ = intent.getStringExtra("univ")
        depart = intent.getStringExtra("depart")
        me = ChatUser(0, nicknames, myIcon)
    }

    private fun initializationChatView() {

        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.primary))
        mChatView.setLeftBubbleColor(R.color.primary)
        mChatView.setBackgroundColor(ContextCompat.getColor(this, R.color.blueGray500))
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.primary_darker))
        mChatView.setSendIcon(R.drawable.ic_action_send)
        mChatView.setRightMessageTextColor(Color.WHITE)
        mChatView.setLeftMessageTextColor(Color.WHITE)
        mChatView.setMessageStatusTextColor(Color.BLACK)
        mChatView.setUsernameTextColor(Color.WHITE)
        mChatView.setSendTimeTextColor(Color.WHITE)
        mChatView.setDateSeparatorColor(Color.WHITE)
        mChatView.inputTextColor = R.color.primary_darker
        mChatView.setInputTextHint("new message...")
        mChatView.setMessageMarginTop(5)
        mChatView.setMessageMarginBottom(5)
        mChatView.setAutoHidingKeyboard(true)
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


           // Send(text).start()

            mChatView.inputText = ""
            Handler().post {
                mChatView.receive(message)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 88) {  //Back Key insertion


            finish()

        }
        if (resultCode == 32) {


            finish()

        }
        if (resultCode == 8808) {
           yourInfo = data?.getSerializableExtra("yourInfo") as UchatInfo

            stoRef = storage.reference.child("profile/${yourInfo.key}.png")
            GlideApp.with(applicationContext).asBitmap().load(stoRef)
                .into(object: SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                       yourIcon=resource
                        you=ChatUser(1,yourInfo.nickname!!,yourIcon)
                    }
                })
//여기부터추가해야함


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

}