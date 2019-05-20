package com.example.halilarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Button;


public class Fragment2 extends Fragment {
    Button armHo;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        //((MainActivity)getActivity()).updateStatusBarColor("#CC1D1D");
        armHo= view.findViewById(R.id.armHo);;
        armHo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AlarmActivity.class));
            }
        });
        return view;
    }

}