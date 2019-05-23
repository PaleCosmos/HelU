package com.example.halilarm


import android.app.Activity
import android.content.Context

import android.content.Intent
import android.content.pm.ActivityInfo

import android.graphics.Color

import android.os.Build
import android.os.Bundle
import android.os.Handler

import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.ArrayAdapter.*
import kotlinx.android.synthetic.main.activity_main.*
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.app_bar_main.*



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var im = R.id.Schedule
    var builder: AlertDialog.Builder? = null
    var dialogView: View? = null
    var backKeyPressedTime: Long = 0L
    var myClass: UserInfo? = null
    lateinit var man:RadioButton
    lateinit var startChat:Button
    lateinit var spin_univ: ArrayAdapter<CharSequence>
    lateinit var spin_dm: ArrayAdapter<CharSequence>
    lateinit var intents: Intent
    lateinit var spinner_parent:Spinner
    lateinit var spinner_child:Spinner
    var choice_univ: String? = null
    var choice_dm: String? = null
    lateinit var header:View
    companion object {
        @JvmStatic
        var frag1: androidx.fragment.app.Fragment? = Fragment1()
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

        header=nav_view2.getHeaderView(0)
        man=header.findViewById(R.id.man)
        man.isChecked = true
        spinner_parent=header.findViewById(R.id.spinner_parent)
        spinner_child=header.findViewById(R.id.spinner_child)
        startChat=header.findViewById(R.id.startChat)
        initialization()
        startChat.setOnClickListener {
            intents = Intent(applicationContext, UchatActivity::class.java)
            intents.putExtra("wantgender", man.isChecked)
            intents.putExtra("key", intent.getStringExtra("key"))
            intents.putExtra("nickname", intent.getStringExtra("nickname"))
            var gendy:String?=null
            if(intent.getBooleanExtra("gender", true))gendy="true"
            else gendy="false"
            intents.putExtra("gender", gendy)
            intents.putExtra("phone", intent.getStringExtra("phone"))
            intents.putExtra("univ", choice_univ)
            intents.putExtra("depart", choice_dm)

            intents.putExtra("myuniv", intent.getStringExtra("university"))

            intents.putExtra("mydepart", intent.getStringExtra("department"))
            startActivityForResult(intents, 1)
            drawer_layout.closeDrawer(GravityCompat.END)
        }

        fab.tag = "DRAG Button"


        fab.setOnClickListener {
            if (currentFocus != null) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
            drawer_layout.openDrawer(GravityCompat.START)
        }

        fab.setOnLongClickListener {
            if (currentFocus != null) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
            drawer_layout.openDrawer(GravityCompat.END)
            true
        }


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
            "${getIntent().getStringExtra("nickname")}님 환영합니다!"

        nav_view.setCheckedItem(R.id.Schedule)
        nav_view.menu.performIdentifierAction(R.id.Schedule, 0)


    }

    private fun initialization() {
        addFragment(frag1)
        addFragment(frag2)
        addFragment(frag3)
        addFragment(frag4)
        hideFragment(frag3)
        hideFragment(frag2)
        hideFragment(frag4)
        showFragment(frag1)

        spin_univ =
            createFromResource(applicationContext, R.array.spinner_univ, android.R.layout.simple_spinner_dropdown_item)
        spinner_parent.adapter = spin_univ
        spinner_parent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spin_univ.getItem(position).toString().equals("가천대학교", ignoreCase = true)) {
                    choice_univ = "가천대학교"
                    spin_dm = createFromResource(
                        view!!.context, R.array.spinner_dm
                        , android.R.layout.simple_spinner_dropdown_item
                    )
                    spinner_child.adapter = spin_dm
                    spinner_child.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            choice_dm = spin_dm.getItem(position).toString()
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
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
            R.id.Schedule -> {

                if (item.itemId != im) {


                    if (frag1 != null) {
                        showFragment(frag1)
                    }
                    if (frag2 != null) {
                        hideFragment(frag2)
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
            R.id.nav_gallery -> {
                if (item.itemId != im) {

                    if (frag1 != null) {
                        hideFragment(frag1)
                    }
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


                    if (frag1 != null) {
                        hideFragment(frag1)
                    }
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


                    if (frag1 != null) {
                        hideFragment(frag1)
                    }
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

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        if (resultCode == 99) {
            setResult(1004)
            finish()
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
