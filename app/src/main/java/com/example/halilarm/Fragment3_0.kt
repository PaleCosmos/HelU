package com.example.halilarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment3_0.*

class Fragment3_0:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment3_0,container,false)

        var V =view?.findViewById<Button>(R.id.chatbut)
        V?.setOnClickListener {


            activity?.supportFragmentManager?.beginTransaction()
                ?.hide(MainActivity.frag3_0!!)?.commit()
            activity?.supportFragmentManager?.beginTransaction()
                ?.show(MainActivity.frag3!!)?.commit()



    }

        return view
    }
}