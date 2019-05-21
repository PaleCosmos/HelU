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

import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import androidx.drawerlayout.widget.DrawerLayout


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    var im = R.id.action_logout
    var builder: AlertDialog.Builder? = null
    var dialogView: View? = null
    var backKeyPressedTime: Long = 0L

    companion object {
        @JvmStatic
        var frag1: androidx.fragment.app.Fragment? = Fragment1()
        @JvmStatic
        var frag2: androidx.fragment.app.Fragment? = Fragment2()
        @JvmStatic
        var frag3: androidx.fragment.app.Fragment? = Fragment3()
        @JvmStatic
        var frag4: androidx.fragment.app.Fragment? = Fragment4()
        @JvmStatic
        var frag3_0: androidx.fragment.app.Fragment? = Fragment3_0()
    }

    // var mWeekView: WeekView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        updateStatusBarColor("#E43F3F")
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        builder = AlertDialog.Builder(this)
        dialogView = layoutInflater.inflate(R.layout.license, null)
        builder?.setView(dialogView)

        initialization()

        fab.tag = "DRAG Button"


        fab.setOnClickListener {
            if(currentFocus!=null){
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(currentFocus?.windowToken, 0)}
            drawer_layout.openDrawer(GravityCompat.START)
        }

        fab.setOnLongClickListener {

            true
        }


        val toggle = object : ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerStateChanged(newState: Int) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if(currentFocus!=null){
                        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                            .hideSoftInputFromWindow(currentFocus?.windowToken, 0)}
                }
            }
        }



        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

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
        addFragment(frag3_0)
        hideFragment(frag3_0)
        hideFragment(frag3)
        hideFragment(frag2)
        hideFragment(frag4)
        showFragment(frag1)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000L) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(applicationContext, "백키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            } else {
                setResult(1004)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.main, menu)
        var nickname = getIntent().getStringExtra("nickname")

        var item: MenuItem = menu.getItem(0)
        item.title = "로그아웃"
        return true
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
                    if (frag3_0 != null) {
                        hideFragment(frag3_0)
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
                    if (frag3_0 != null) {
                        hideFragment(frag3_0)
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
                        hideFragment(frag3)
                    }
                    if (frag4 != null) {
                        hideFragment(frag4)
                    }
                    if (frag3_0 != null) {
                        showFragment(frag3_0)
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
                    if (frag3_0 != null) {
                        hideFragment(frag3_0)
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
    }

    fun replaceFragment(fragment: androidx.fragment.app.Fragment?) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentS, fragment!!).commit()
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
