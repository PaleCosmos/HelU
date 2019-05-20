package com.example.halilarm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fragment4 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment4, container, false);

        Button btbt = view.findViewById(R.id.btbt);
        final TextView tvtv = view.findViewById(R.id.tvtv);
        final EditText eded = view.findViewById(R.id.eet);

        btbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = eded.getText().toString();
                tvtv.append("\n" + msg);
            }
        });

        return view;
    }
}