package com.example.halilarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter.createFromResource
import androidx.fragment.app.Fragment

class Fragment1 : Fragment() {
    lateinit var spin_univ: ArrayAdapter<CharSequence>
    lateinit var spin_dm: ArrayAdapter<CharSequence>
    lateinit var spinner_univ: Spinner
    lateinit var spinner_dm: Spinner
    lateinit var but: Button
    var choice_univ = ""
    var choice_dm = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment1, container, false) as ViewGroup
        initialization(view)
        but.setOnClickListener {
            Toast.makeText(view!!.context,"$choice_univ $choice_dm",Toast.LENGTH_SHORT).show()
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