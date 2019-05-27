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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
    View.OnLongClickListener {

    var im = R.id.nav_gallery
    var builder: AlertDialog.Builder? = null
    var dialogView: View? = null
    var backKeyPressedTime: Long = 0L
    var myClass: UserInfo? = null
    lateinit var man: RadioButton
    lateinit var startChat: Button
    lateinit var adapter_univ: ArrayAdapter<CharSequence>
    lateinit var adapter_depart: ArrayAdapter<CharSequence>
    lateinit var intents: Intent
    lateinit var spinner_parent: Spinner
    lateinit var spinner_child: Spinner
    private lateinit var profile: CircleImageView
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var authReference: StorageReference
    lateinit var uidReference: StorageReference
    var myInfos: UserInfo? = null
    var choice_univ: String? = null
    var choice_dm: String? = null
    var isFabOpen = false
    var initFrag = 0  // 0-> frag2 ,1->frag3, 2->frag4
    lateinit var header: View
    lateinit var myUid: String

    var FLAG = 0

    companion object {
        @JvmStatic
        var frag2: androidx.fragment.app.Fragment? = Fragment2()
        @JvmStatic
        var frag3: androidx.fragment.app.Fragment? = Fragment3()
        @JvmStatic
        var frag4: androidx.fragment.app.Fragment? = Fragment4()
    }

    // var mWeekView: WeekView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        updateStatusBarColor("#CC1D1D")
        setContentView(R.layout.activity_main)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //setSupportActionBar(toolbar)
        builder = AlertDialog.Builder(this)
        dialogView = layoutInflater.inflate(R.layout.license, null)
        builder?.setView(dialogView)

        storage = FirebaseStorage.getInstance("gs://palecosmos-helu.appspot.com/")
        storageReference = storage.reference
        authReference = storageReference.child("profile")

        myUid = intent.getStringExtra("key") // myUID

        uidReference = authReference.child("$myUid.png")

        myInfos = intent.getSerializableExtra("USERINFO") as UserInfo?



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


            /*
            intents.putExtra("nickname", myInfos.nickname)
            var gendy: String? = ""
            if (myInfos.gender!!) gendy = "true"
            else gendy = "false"
            intents.putExtra("gender", gendy)
            intents.putExtra("phone",myInfos.phone)
            intents.putExtra("myuniv", myInfos.university)
            intents.putExtra("mydepart", myInfos.department)
*/
            intents.putExtra("USERINFO", myInfos)
            intents.putExtra("univ", choice_univ)
            intents.putExtra("depart", choice_dm)

            startActivityForResult(intents, 1)
            drawer_layout.closeDrawer(GravityCompat.END)
        }

        fab.tag = "DRAG Button"


/*                                                       toolbar = null*/
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
            "${(intent.getSerializableExtra("USERINFO") as UserInfo).nickname} 님 환영합니다!"

        profile = nav_view.getHeaderView(0).findViewById(R.id.imageViewss)

        // fabStart.isVisible=false
        profile.setOnClickListener {
            var tents = Intent(applicationContext, ProfileActivity::class.java)
            startActivityForResult(tents, 135)
        }

        var fs = FirebaseStorage.getInstance()
        var imagesRef = fs.reference.child("profile/$myUid.png")


        GlideApp.with(applicationContext)
            .load(imagesRef)
            .override(100, 100)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(profile)

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
                        var vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibe.vibrate(40)
                    }
                    1 -> {
                        if (frag2 != null) {
                            showFragment(frag2)
                        }
                        if (frag3 != null) {
                            hideFragment(frag3)
                        }
                        if (frag4 != null) {
                            hideFragment(frag4)
                        }

                        im = R.id.nav_gallery
                        nav_view.setCheckedItem(im)
                        initFrag = 0
                    }
                    2 -> {

                        if (frag2 != null) {
                            hideFragment(frag2)
                        }
                        if (frag3 != null) {
                            showFragment(frag3)
                        }
                        if (frag4 != null) {
                            hideFragment(frag4)
                        }
                        im = R.id.nav_slideshow
                        nav_view.setCheckedItem(im)
                        initFrag = 1
                    }
                }
            }
            R.id.fab2 -> {
                when (initFrag) {
                    0 -> {


                        if (frag2 != null) {
                            hideFragment(frag2)
                        }
                        if (frag3 != null) {
                            showFragment(frag3)
                        }
                        if (frag4 != null) {
                            hideFragment(frag4)
                        }
                        im = R.id.nav_slideshow
                        nav_view.setCheckedItem(im)
                        initFrag = 1
                    }
                    1 -> {

                        if (frag2 != null) {
                            hideFragment(frag2)
                        }
                        if (frag3 != null) {
                            hideFragment(frag3)
                        }
                        if (frag4 != null) {
                            showFragment(frag4)
                        }

                        im = R.id.nav_manage
                        nav_view.setCheckedItem(im)
                        initFrag = 2
                    }
                    2 -> {
                        var vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibe.vibrate(40)
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
                var vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibe.vibrate(40)
            }

        }
        return true
    }

    fun rotateFab() {
        if (isFabOpen) {
            fab.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_backward));
            fab2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
            fab3.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close));
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward));
            fab2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
            fab3.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open));
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;

        }

    }

    private fun initialization() {


        addFragment(frag2)
        addFragment(frag3)
        addFragment(frag4)
        hideFragment(frag3)
        hideFragment(frag4)
        showFragment(frag2)
        fab.setOnLongClickListener(this)
        fab.setOnClickListener(this)
        fab2.setOnClickListener(this)
        fab3.setOnClickListener(this)
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
        // Handle navigation view item clicks here.


        when (item.itemId) {

            R.id.nav_gallery -> {
                if (item.itemId != im) {

                    if (frag2 != null) {
                        showFragment(frag2)
                    }
                    if (frag3 != null) {
                        hideFragment(frag3)
                    }
                    if (frag4 != null) {
                        hideFragment(frag4)
                    }

                    im = item.itemId
                }

            }
            R.id.nav_slideshow -> {
                if (item.itemId != im) {


                    if (frag2 != null) {
                        hideFragment(frag2)
                    }
                    if (frag3 != null) {
                        showFragment(frag3)
                    }
                    if (frag4 != null) {
                        hideFragment(frag4)
                    }
                    im = item.itemId
                }

            }
            R.id.nav_manage -> {
                if (item.itemId != im) {


                    if (frag2 != null) {
                        hideFragment(frag2)
                    }
                    if (frag3 != null) {
                        hideFragment(frag3)
                    }
                    if (frag4 != null) {
                        showFragment(frag4)
                    }

                    im = item.itemId
                }
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

            uidReference.delete()
            var bitg = MediaStore.Images.Media.getBitmap(
                contentResolver,
                profileUri
            )

            val baos = ByteArrayOutputStream()
            bitg.compress(Bitmap.CompressFormat.PNG, 100, baos)
            uidReference.putBytes(baos.toByteArray())

            var myFile = File(profileUri.path)
            if (myFile.exists()) myFile.delete()

        } else if (resultCode == 7979) {
            var friendKey = data?.getSerializableExtra("friend") as UchatInfo

            //친구추가창
        }else if(resultCode==7978)
        {
            var friendKey = data?.getSerializableExtra("friend") as UchatInfo

            //친구추가창
        }
    }


    fun addFragment(fragment: androidx.fragment.app.Fragment?) {
        supportFragmentManager.beginTransaction().add(R.id.fragmentS, fragment!!).commit()
    }

    fun hideFragment(fragment: androidx.fragment.app.Fragment?) {
        supportFragmentManager.beginTransaction().hide(fragment!!).commit()
    }

    fun showFragment(fragment: androidx.fragment.app.Fragment?) {
        supportFragmentManager.beginTransaction().show(fragment!!).commit()
    }

    fun updateStatusBarColor(color: String) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor(color)
        }
    }
}
