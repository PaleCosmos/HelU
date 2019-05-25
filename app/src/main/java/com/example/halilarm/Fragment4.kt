package com.example.halilarm

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.w3c.dom.Text
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class Fragment4: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment4, container, false) as ViewGroup

        return view
    }

}