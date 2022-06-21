package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ProfileScreen extends Fragment {

    ProfileScreen(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        ImageButton btnInputMyProfile = (ImageButton) v.findViewById(R.id.btn_my_input_profile);
        Button btnConfirm = (Button) v.findViewById(R.id.confirm);
        EditText myIntroduce = (EditText) v.findViewById(R.id.myIntroduce);
        EditText myName = (EditText) v.findViewById(R.id.myName);
        try {
            JSONObject myInfo = Data.readMyInfo();
            myName.setText(myInfo.getString("Nickname").toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMyIntroduce = myIntroduce.getText().toString();
                String strMyName = myName.getText().toString();
                //서버에 post
            }
        });

        btnInputMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InputProfileActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

}
