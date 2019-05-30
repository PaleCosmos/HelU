package com.pale_cosmos.helu


import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pale_cosmos.helu.util.myUtil
import kotlinx.android.synthetic.main.activity_u_chat.*
import java.io.ByteArrayOutputStream
import java.io.File

class UchatActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var database: FirebaseDatabase

    var dataRef: DatabaseReference? = null
    lateinit var myDataRef: DatabaseReference
    var myquitcheck = true
    lateinit var nicknames: String
    lateinit var key: String
    lateinit var univ: String
    lateinit var depart: String
    lateinit var me: ChatUser
    lateinit var you: ChatUser
    var myInfo: UserInfo? = null
    lateinit var wantgenderString: String
    lateinit var intents: Intent
    var yourInfo: UchatInfo? = null
    var wantgender = true
    var myId: Int = 0
    lateinit var myIcon: Bitmap
    var yourId = 1
    lateinit var yourIcon: Bitmap
    var areyou = true
    var backKeyPressedTime: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        areyou = true
        updateStatusBarColor("#E43F3F")
        setContentView(R.layout.activity_u_chat)
        initializationChatView()
        initialization()
    }

    private fun initialization() {

        setValue()
        intents = Intent(this@UchatActivity, SocketReceiveDialog::class.java)
//        intents.putExtra("USERINFO", myInfo)
        var holderId = myUtil.putDataHolder(myInfo)

        intents.putExtra("USERINFO", holderId)

        intents.putExtra("key", key)
        intents.putExtra("univ", univ)
        intents.putExtra("depart", depart)
        intents.putExtra("wantgender", wantgenderString)


        Log.d("INTENTERROR","UCHATACTIVITY TO SOCKETRECEIVEDIALOG")
        startActivityForResult(intents, 1)
        database = FirebaseDatabase.getInstance()


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
//        myInfo = intent.getSerializableExtra("USERINFO") as UserInfo?
        Log.d("INTENTERROR","UCHATACTIVITY FROM MAINACTIVITY")
        var holderId = intent.getStringExtra(myUtil.myUserInfo)
        myInfo = myUtil.popDataHolder(holderId) as UserInfo

        key = intent.getStringExtra("key")
        database = FirebaseDatabase.getInstance()
        myDataRef = database.reference.child("chats").child(key).child("Uchat")

        univ = intent.getStringExtra("univ")
        depart = intent.getStringExtra("depart")
        me = ChatUser(0, myInfo?.nickname!!, myIcon)
    }

    private fun deleteChildListener(ref: DatabaseReference) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {


            }

            override fun onChildRemoved(p0: DataSnapshot) {
                if (myquitcheck) {
                    var res = Intent(this@UchatActivity, BackKeyPress::class.java)
                    res.putExtra("code", 2)
                    startActivityForResult(res, 1)
                    deleteMyLog()
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
        })
    }

    private fun addChildListener(ref: DatabaseReference) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val data = p0.getValue(ChatValue::class.java)

                when (data?.type) {
                    "message" -> {
                        if (areyou) (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(40)
                        areyou = false
                        var message = Message.Builder()
                            .setUser(you)
                            .setRight(false)
                            .setText(data.message!!)
                            .hideIcon(false)
                            .build()
                        Handler().post {
                            mChatView.receive(message)
                        }
                    }
                    "photo" -> {
                        if (areyou) (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(40)
                        areyou = false

                        var message = Message.Builder()
                            .setUser(you)
                            .setRight(false)
                            .setType(Message.Type.PICTURE)
                            .setPicture(myUtil.stringToBitmap(data?.photo))
                            .setText("")
                            .hideIcon(false)
                            .build()
                        Handler().post {
                            mChatView.receive(message)
                        }
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
        })
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
        mChatView.setOnClickOptionButtonListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var tents = Intent(this@UchatActivity, ProfileActivity::class.java)
                tents.putExtra("by", 2)
                startActivityForResult(tents, 37)
            }
        })
        mChatView.setOptionButtonColor(R.color.primary_darker)
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
            areyou = true
            var message = Message.Builder()
                .setUser(me)
                .setRight(true)
                .setText(text)
                .hideIcon(true)
                .build()

            if (dataRef != null) {
                val msg = ChatValue()
                msg.type = "message"
                msg.key = key
                msg.message = text
                msg.photo = ""
                dataRef?.push()?.setValue(msg)
            }
            mChatView.inputText = ""
            Handler().post {
                mChatView.receive(message)
            }
        }


    }

    private fun sendImage(bitmap: Bitmap) {
        areyou = true
        var message = Message.Builder()
            .setUser(me)
            .setRight(true)
            .setType(Message.Type.PICTURE)
            .setPicture(bitmap)
            .setText("")
            .hideIcon(true)
            .build()

        if (dataRef != null) {
            val msg = ChatValue()
            msg.type = "photo"
            msg.key = key
            msg.message = ""
            msg.photo = myUtil.bitmapToString(bitmap)
            dataRef?.push()?.setValue(msg)
        }
        mChatView.inputText = ""
        Handler().post {
            mChatView.receive(message)
        }
    }

    private fun deleteMyLog() {
        myDataRef.removeValue()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            88 -> {

                var res = Intent()
                var holderId = myUtil.putDataHolder(yourIcon)


//                res.putExtra("friend", yourInfo)
                res.putExtra("friend", yourInfo)
                res.putExtra("icon", holderId)
                setResult(7978, res)
                finish()

            }
            32 -> {
                finish()
                myquitcheck = false
            }
            8080 -> {
                yourInfo = data?.getSerializableExtra("yourInfo") as UchatInfo?
                var holderId = data?.getStringExtra("icon")!!

                yourIcon = myUtil.popDataHolder(holderId) as Bitmap

                you = ChatUser(1, yourInfo?.nickname!!, yourIcon)
                addChildListener(myDataRef)
                dataRef = database.reference.child("chats").child("${yourInfo?.key}").child("Uchat")
                deleteChildListener(dataRef!!)
            }
            8081 -> {
                startActivityForResult(Intent(this@UchatActivity, ConnectingException::class.java), 1)
            }
            8082 -> {
                startActivityForResult(Intent(this@UchatActivity, ConnectingException::class.java), 1)
            }
            10043 -> { // 친구추가
                var res = Intent()
                res.putExtra("friend", yourInfo)
                res.putExtra("icon", yourIcon)
                setResult(7979, res)
                myquitcheck = false
                finish()
            }
            10044 -> {
                myquitcheck = false
                finish()
            }
            337->finish()
            75 -> {
                var profileUri = data?.getParcelableExtra("profileUri") as Uri


                var bitg = MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    profileUri
                )

                sendImage(bitg)

                var myFile = File(profileUri.path)
                if (myFile.exists()) myFile.delete()

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        deleteMyLog()
        myquitcheck = false
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