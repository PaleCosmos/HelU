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
        initialization(view)
        key=arguments?.getString("key")
        nick=arguments?.getString("nickname")
        var genders=arguments!!.getBoolean("gender",true)
        if(genders)gender="true"
        else gender="false"
        phone=arguments?.getString("phone")

        myuniv=arguments?.getString("university")

        mydepart=arguments?.getString("department")


        man=view.findViewById(R.id.man)
        woman=view.findViewById(R.id.woman)
        man?.isChecked=true

        but.setOnClickListener {

            intents = Intent(view.context,UchatActivity::class.java)
            intents.putExtra("wantgender",man?.isChecked)
            intents.putExtra("key",key)
            intents.putExtra("nickname",nick)

            intents.putExtra("gender",gender)
            intents.putExtra("phone",phone)
            intents.putExtra("univ",choice_univ)
            intents.putExtra("depart",choice_dm)

            intents.putExtra("myuniv",myuniv)

            intents.putExtra("mydepart",mydepart)
            startActivityForResult(intents,1)
        }
        return view
    }

    private fun initialization(view:ViewGroup) {
        spinner_univ = view.findViewById(R.id.spinner_parent)
        spinner_dm = view.findViewById(R.id.spinner_child)
        but = view.findViewById(R.id.startChat)
        spin_univ = createFromResource(
            view!!.context, R.array.spinner_univ
            , android.R.layout.simple_spinner_dropdown_item
        )
        spinner_univ.adapter = spin_univ
        spinner_univ.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spin_univ.getItem(position).toString().equals("가천대학교", ignoreCase = true)) {
                    choice_univ = "가천대학교"
                    spin_dm = createFromResource(
                        view!!.context, R.array.spinner_dm
                        , android.R.layout.simple_spinner_dropdown_item
                    )
                    spinner_dm.adapter = spin_dm
                    spinner_dm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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


}