package com.example.roomate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

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
        Dialog profileSelect = new Dialog(getActivity());
        profileSelect.setContentView(R.layout.profileimageselect_dialog);

        ImageButton btnInputMyProfile = (ImageButton) v.findViewById(R.id.btn_my_input_profile);
        Button btnConfirm = (Button) v.findViewById(R.id.confirm);
        EditText myIntroduce = (EditText) v.findViewById(R.id.myIntroduce);
        EditText myName = (EditText) v.findViewById(R.id.myName);
        ImageButton myProfileImage = (ImageButton) v.findViewById(R.id.profile_myprofile);
        Spinner sp_grade = (Spinner) v.findViewById(R.id.sp_grade);
        Spinner sp_age = (Spinner) v.findViewById(R.id.sp_age);
        Spinner sp_dom = (Spinner) v.findViewById(R.id.sp_dom);
        Spinner sp_gender = (Spinner) v.findViewById(R.id.sp_gender);

        try {
            JSONObject myInfo = Data.readMyInfo();
            myName.setText(myInfo.getString("Nickname"));
            myIntroduce.setText(myInfo.getString("Introduce"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMyIntroduce = myIntroduce.getText().toString();
                String strMyName = myName.getText().toString();
                int profileId = myProfileImage.getId();
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
        myProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileSelect.show();
                profileSelect.findViewById(R.id.profileImage1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myProfileImage.setImageResource(R.drawable.a);
                        profileSelect.dismiss();
                    }
                });
                profileSelect.findViewById(R.id.profileImage2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myProfileImage.setImageResource(R.drawable.b);
                        profileSelect.dismiss();
                    }
                });
                profileSelect.findViewById(R.id.profileImage3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myProfileImage.setImageResource(R.drawable.c);
                        profileSelect.dismiss();
                    }
                });
            }
        });
        return v;
    }

}
