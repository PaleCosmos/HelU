package com.pale_cosmos.helu


import android.app.Activity
import android.content.ContentUris
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
import android.util.Log

import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.view.*

import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.ArrayAdapter.*
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pale_cosmos.helu.util.myUtil
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment2.*
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
    View.OnLongClickListener {

    var im = R.id.nav_gallery
    var builder: AlertDialog.Builder? = null
    var dialogView: View? = null
    var backKeyPressedTime: Long = 0L
    lateinit var imageLog: Bitmap
    lateinit var chatlog: UchatInfo
    lateinit var man: RadioButton
    lateinit var startChat: Button
    lateinit var adapter_univ: ArrayAdapter<CharSequence>
    lateinit var adapter_depart: ArrayAdapter<CharSequence>
    lateinit var intents: Intent
    lateinit var spinner_parent: Spinner
    lateinit var spinner_child: Spinner
    private lateinit var profile: CircleImageView
    //    lateinit var storage: FirebaseStorage
//    lateinit var storageReference: StorageReference
//    lateinit var authReference: StorageReference
//    lateinit var uidReference: StorageReference
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    var myInfos: UserInfo? = null
    var choice_univ: String? = null
    var choice_dm: String? = null
    var isFabOpen = false
    var myFreind = mutableListOf<Friends>()
    var initFrag = 0  // 0-> frag2 ,1->frag3, 2->frag4
    lateinit var header: View
    lateinit var myUid: String


    var frag2: androidx.fragment.app.Fragment = Fragment2()

    var frag3: androidx.fragment.app.Fragment = Fragment3()

    var frag4: androidx.fragment.app.Fragment = Fragment4()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        myUtil.updateStatusBarColor(window, "#CC1D1D")
        setContentView(R.layout.activity_main)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        builder = AlertDialog.Builder(this)
        dialogView = layoutInflater.inflate(R.layout.license, null)
        builder?.setView(dialogView)
        database = FirebaseDatabase.getInstance()
//        storage = FirebaseStorage.getInstance(myUtil.storageAddress)
//        storageReference = storage.reference
//        authReference = storageReference.child("profile")

        myUid = intent.getStringExtra("key") // myUID
        databaseReference = database.reference.child("users").child("$myUid").child("friends")

//        uidReference = authReference.child("$myUid.png")
//        myInfos = intent.getSerializableExtra(myUtil.myUserInfo) as UserInfo?
        var holderId = intent.getStringExtra(myUtil.myUserInfo)
        myInfos = myUtil.popDataHolder(holderId) as UserInfo
        header = nav_view2.getHeaderView(0)
        man = header.findViewById(R.id.man)
        man.isChecked = true
        spinner_parent = header.findViewById(R.id.spinner_parent)
        spinner_child = header.findViewById(R.id.spinner_child)
        startChat = header.findViewById(R.id.startChat)
        initialization()

        startChat.setOnClickListener {
            intents = Intent(applicationContext, UchatActivity::class.java)
            intents.putExtra("wantgender", man.isChecked)

            intents.putExtra("key", myUid)
/////////////////////////////////////////////////////////////////////
//            intents.putExtra(myUtil.myUserInfo, myInfos)
            var holderId = myUtil.putDataHolder(myInfos)

            intents.putExtra(myUtil.myUserInfo, holderId)

            intents.putExtra("univ", choice_univ)
            intents.putExtra("depart", choice_dm)
            Log.d("INTENTERROR", "MAINACTIVITY TO UCHATACTIVITY")
            startActivityForResult(intents, 1)
            drawer_layout.closeDrawer(GravityCompat.END)
        }
        fab.tag = "DRAG Button"
        val toggle = object : ActionBarDrawerToggle(
            this, drawer_layout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {


            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (currentFocus != null) {
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    }

                }
            }
        }

        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view2.setNavigationItemSelectedListener {
            true
        }
        nav_view.getHeaderView(0).findViewById<TextView>(R.id.myNicknamess).text =
            "${myInfos?.nickname} 님 환영합니다!"

        profile = nav_view.getHeaderView(0).findViewById(R.id.imageViewss)

        // fabStart.isVisible=false
        profile.setOnClickListener {
            var tents = Intent(applicationContext, ProfileActivity::class.java)
            tents.putExtra("by", 1)
            startActivityForResult(tents, 135)
        }

        var fs = FirebaseStorage.getInstance()
        var imagesRef = fs.reference.child("profile/$myUid.png")


//        GlideApp.with(applicationContext).asBitmap().load(imagesRef)
//            .override(100, 100)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .skipMemoryCache(true)
//            .placeholder(R.drawable.profile)
//            .error(R.drawable.profile)
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//                ) {
//                    profile.setImageBitmap(resource)
//
//                }
//            })

        database.reference.child("users").child("$myUid").child("photo")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    profile.setImageBitmap(myUtil.stringToBitmap(p0.getValue(String::class.java)!!))
                    frag2.view?.findViewById<CircleImageView>(R.id.friendPhotoImg)
                        ?.setImageBitmap(myUtil.stringToBitmap(p0.getValue(String::class.java)!!))
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })




        nav_view.setCheckedItem(R.id.nav_gallery)
        nav_view.menu.performIdentifierAction(R.id.nav_gallery, 0)

    }


    override fun onClick(v: View?) {
        var id = v?.id
        when (id) {
            R.id.fab -> {
                if (currentFocus != null) {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }
                drawer_layout.openDrawer(GravityCompat.START)
            }

            R.id.fab3 -> { // up
                when (initFrag) {
                    0 -> {
                        myUtil.viberate(applicationContext, 40)
                    }
                    1 -> {
                        im = myUtil.rotateFragment(R.id.nav_gallery, supportFragmentManager, frag2, frag3, frag4)
                        nav_view.setCheckedItem(im)
                        initFrag = 0
                    }
                    2 -> {
                        im = myUtil.rotateFragment(R.id.nav_slideshow, supportFragmentManager, frag3, frag2, frag4)
                        nav_view.setCheckedItem(im)
                        initFrag = 1
                    }
                }
            }
            R.id.fab2 -> {
                when (initFrag) {
                    0 -> {
                        im = myUtil.rotateFragment(R.id.nav_slideshow, supportFragmentManager, frag3, frag2, frag4)
                        nav_view.setCheckedItem(im)
                        initFrag = 1
                    }
                    1 -> {
                        im = myUtil.rotateFragment(R.id.nav_manage, supportFragmentManager, frag4, frag2, frag3)
                        nav_view.setCheckedItem(im)
                        initFrag = 2
                    }
                    2 -> {
                        myUtil.viberate(applicationContext, 40)
                    }
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        var id = v?.id
        when (id) {
            R.id.fab -> {
                rotateFab()
                myUtil.viberate(applicationContext, 100)
            }

        }
        return true
    }

    private fun rotateFab() {
        when (isFabOpen) {
            true -> {
                myUtil.rotateTrue(applicationContext, fab, fab2, fab3, R.anim.rotate_backward, R.anim.fab_close)
                isFabOpen = false
            }
            false -> {
                myUtil.rotateFalse(applicationContext, fab, fab2, fab3, R.anim.rotate_forward, R.anim.fab_open)
                isFabOpen = true
            }
        }
    }

    private fun addListener() {
        fab.setOnLongClickListener(this)
        fab.setOnClickListener(this)
        fab2.setOnClickListener(this)
        fab3.setOnClickListener(this)
    }

    private fun initialization() {


        var bd = Bundle()
        var holderId = myUtil.putDataHolder(myInfos)

        bd.putString("myInfo", holderId)
        bd.putString("key", myUid)

        frag2.arguments = bd
        myUtil.initFragment(R.id.fragmentS, supportFragmentManager, frag2, frag3, frag4)
        addListener()



        //databaseReference.add
        //db읽어오기


        adapter_univ =
            createFromResource(applicationContext, R.array.spinner_univ, R.layout.spinner_item)
        adapter_univ.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner_parent.adapter = adapter_univ
        spinner_parent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (adapter_univ.getItem(position).toString().equals("가천대학교", ignoreCase = true)) {
                    choice_univ = "가천대학교"
                    adapter_depart = createFromResource(
                        view!!.context, R.array.spinner_dm
                        , R.layout.spinner_item
                    )
                    adapter_depart.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    spinner_child.adapter = adapter_depart
                    spinner_child.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            choice_dm = adapter_depart.getItem(position).toString()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START) || drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawers()
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000L) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(applicationContext, "나갈거야?", Toast.LENGTH_SHORT).show()
                return
            } else {
                val back = Intent(this, BackKeyPress::class.java)
                back.putExtra("code", 1)
                startActivityForResult(back, 3)
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_gallery -> {
                if (item.itemId != im) im =
                    myUtil.rotateFragment(R.id.nav_gallery, supportFragmentManager, frag2, frag3, frag4)
            }
            R.id.nav_slideshow -> {
                if (item.itemId != im) im =
                    myUtil.rotateFragment(R.id.nav_slideshow, supportFragmentManager, frag3, frag2, frag4)

            }
            R.id.nav_manage -> {
                if (item.itemId != im) im =
                    myUtil.rotateFragment(R.id.nav_manage, supportFragmentManager, frag4, frag2, frag3)

            }
            R.id.nav_share -> {
                startActivityForResult(Intent(applicationContext, LogoutActivity::class.java), 1)
            }
            R.id.License -> {
                Handler().post(Runnable {
                    startActivity(Intent(applicationContext, LicenseActivity::class.java))
                    // builder?.show()
                })
            }
            R.id.Schedule -> {
                drawer_layout.openDrawer(GravityCompat.END)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else if (resultCode == 99) {
            finish()
        } else if (resultCode == 75) {
            var profileUri = data?.getParcelableExtra("profileUri") as Uri

            GlideApp.with(applicationContext)
                .load(profileUri)
                .override(100, 100)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profile)

            GlideApp.with(applicationContext)
                .load(profileUri)
                .override(100, 100)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(frag2.view?.findViewById(R.id.friendPhotoImg)!!)

//            uidReference.delete()
            var bitg = MediaStore.Images.Media.getBitmap(
                contentResolver,
                profileUri
            )

//            val baos = ByteArrayOutputStream()
//            bitg.compress(Bitmap.CompressFormat.PNG, 100, baos)
//            uidReference.putBytes(baos.toByteArray())
            database.reference.child("users").child("$myUid").child("photo").setValue(myUtil.bitmapToString(bitg))

            var myFile = File(profileUri.path)
            if (myFile.exists()) myFile.delete()

        } else if (resultCode == 7979) {
            var friendy = data?.getSerializableExtra("friend") as UchatInfo
            var holderId = data?.getStringExtra("icon")
            var bit = myUtil.popDataHolder(holderId) as Bitmap
            var myfriend = Friends()
            myfriend.setValue(
                friendy.nickname!!,
                friendy.key!!,
                friendy.phone!!,
                myUtil.bitmapToString(bit),
                friendy.university!!,
                friendy.department!!
            )
            //어댑터에추가
            Fragment2.myList.add(myfriend)
            Fragment2.myAdapter.notifyDataSetChanged()

            databaseReference.child(myfriend.key).removeValue()
            databaseReference.child(myfriend.key).setValue(myfriend)
            //친구추가창
        } else if (resultCode == 7070) {
            var friendy = chatlog
//            var holderId = data?.getStringExtra("icon")!!
            var bit = imageLog
            var myfriend = Friends()
            myfriend.setValue(
                friendy.nickname!!,
                friendy.key!!,
                friendy.phone!!,
                myUtil.bitmapToString(bit!!),
                friendy.university!!,
                friendy.department!!
            )
            databaseReference.child(myfriend.key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    var x = p0.getValue(Friends::class.java)
                    when (x?.key == myfriend.key) {
                        true -> {
                            Toast.makeText(this@MainActivity, "우린 이미 친구에요!", Toast.LENGTH_SHORT).show()
                        }
                        false -> {
                            Fragment2.myList.add(myfriend)
                            Fragment2.myAdapter.notifyDataSetChanged()
                            databaseReference.child(myfriend.key).setValue(myfriend)
                        }

                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })


            //databaseReference.child(myfriend.key).removeValue()

        } else if (resultCode == 7978) {
            var intents = Intent(this@MainActivity, BackKeyPress::class.java)
            chatlog = data?.getSerializableExtra("friend") as UchatInfo

            var holderId = data?.getStringExtra("icon")
            imageLog = myUtil.popDataHolder(holderId) as Bitmap

            intents.putExtra("code", 3)
            startActivityForResult(intents, 1)
        }
    }
}
