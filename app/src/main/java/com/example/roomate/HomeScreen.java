package com.example.roomate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreen extends Fragment {


    public HomeScreen() {
        // Required empty public constructor
    }

    public static HomeScreen newInstance(String param1, String param2) {
        HomeScreen fragment = new HomeScreen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        ImageButton button_Domitory_3to5 = (ImageButton) v.findViewById(R.id.btn_dm_3to5);
        ImageButton button_Domitory_8 = (ImageButton) v.findViewById(R.id.btn_dm_8);
        ImageButton button_Domitory_9 = (ImageButton) v.findViewById(R.id.btn_dm_9);

        button_Domitory_3to5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
                getActivity().startActivity(intent);
            }
        });

        button_Domitory_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Domitory_8.class);
                // activity간 자료공유 key, value 넣고 이동한 activity에서
                //get intent, get String Extra로 값을 주고받을 수 있음
                intent.putExtra("test","test해보겠읍니다");

                getActivity().startActivity(intent);
            }
        });

        button_Domitory_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Domitory_9.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

}


