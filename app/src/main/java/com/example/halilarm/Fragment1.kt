package com.example.halilarm

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter.createFromResource
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment1.*

class Fragment1 : Fragment() {
    lateinit var spin_univ: ArrayAdapter<CharSequence>
    lateinit var spin_dm: ArrayAdapter<CharSequence>
    lateinit var spinner_univ: Spinner
    lateinit var spinner_dm: Spinner
    lateinit var but: Button
    lateinit var intents:Intent
    var key:String?=null
    var nick:String?=null
    var gender:String?=null
    var phone:String?=null
    var myuniv:String?=null
    var mydepart:String?=null
    var choice_univ = ""
    var choice_dm = ""
    var man:RadioButton?=null
    var woman:RadioButton?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment1, container, false) as ViewGroup

        return view
    }


}