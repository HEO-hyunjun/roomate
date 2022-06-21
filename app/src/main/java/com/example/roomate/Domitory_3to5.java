/*
05.25 박우천
Domitory_3to5는 기숙사 3~5동 목록입니다. 다른 8동 9동은 변수명 빼고는 코드가 동일합니다.
뒤로가기와 Recyclerview로 목록 생성이 되는 것까지 구현했습니다. 이후에 DB를 통해 가져오도록 수정해야 합니다.
 */

package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class Domitory_3to5 extends AppCompatActivity {


    public static final int REQUEST_CODE_MENU = 101;
    private RecyclerAdapter adapter; // RecyclerAdapter 선언

    Dialog filterDialog;
    ArrayList<Integer> tags = new ArrayList<>();
    ArrayList<Integer> myTags = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domitory3to5);
        ImageButton button_backHome = findViewById(R.id.btn_backtohome); // 뒤로가기 버튼 선언
        ImageButton button_filter = findViewById(R.id.btn_filter);


        filterDialog = new Dialog(Domitory_3to5.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filter_dialog);


        init();//Recyclerview의 adapter 불러오기
        getData();//Data 불러오기
        //getData(reqestProfiles(myTags));
        //뒤로가기
        button_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        button_filter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                filterDialog.show();
                filterDialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Data forChipToTag = new Data();

                        ChipGroup personalityChgrp = (ChipGroup)filterDialog.findViewById((R.id.personalityChipGroup));
                        Chip personalityCh = (Chip)filterDialog.findViewById(personalityChgrp.getCheckedChipId());
                        ChipGroup wakeuptimeChgrp = (ChipGroup)filterDialog.findViewById((R.id.wakeuptimeChipGroup));
                        Chip wakeuptimeCh = (Chip)filterDialog.findViewById(wakeuptimeChgrp.getCheckedChipId());
                        ChipGroup sleeptimeChgrp = (ChipGroup)filterDialog.findViewById((R.id.sleeptimeChipGroup));
                        Chip sleeptimeCh = (Chip)filterDialog.findViewById(sleeptimeChgrp.getCheckedChipId());
                        ChipGroup snoringChgrp = (ChipGroup)filterDialog.findViewById((R.id.snoringChipGroup));
                        Chip snoringCh = (Chip)filterDialog.findViewById(snoringChgrp.getCheckedChipId());
                        ChipGroup noiseChgrp = (ChipGroup)filterDialog.findViewById((R.id.noiseChipGroup));
                        Chip noiseCh = (Chip)filterDialog.findViewById(noiseChgrp.getCheckedChipId());
                        ChipGroup hygieneChgrp = (ChipGroup)filterDialog.findViewById((R.id.hygieneChipGroup));
                        Chip hygieneCh = (Chip)filterDialog.findViewById(hygieneChgrp.getCheckedChipId());
                        ChipGroup smokingChgrp = (ChipGroup)filterDialog.findViewById((R.id.smokingChipGroup));
                        Chip smokingCh = (Chip)filterDialog.findViewById(smokingChgrp.getCheckedChipId());

                        tags.add(forChipToTag.parseTagToInt(personalityCh.getText().toString(), 0));
                        tags.add(forChipToTag.parseTagToInt(hygieneCh.getText().toString(), 1));
                        tags.add(forChipToTag.parseTagToInt(noiseCh.getText().toString(), 2));
                        tags.add(forChipToTag.parseTagToInt(wakeuptimeCh.getText().toString(), 3));
                        tags.add(forChipToTag.parseTagToInt(sleeptimeCh.getText().toString(), 4));
                        tags.add(forChipToTag.parseTagToInt(snoringCh.getText().toString(), 5));
                        tags.add(forChipToTag.parseTagToInt(smokingCh.getText().toString(), 6));
                        //testView.setText(tags.toString());
                        //post tags
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("Personality",tags.get(0));
                            jsonObject.put("Hygiene",tags.get(1));
                            jsonObject.put("Noise",tags.get(2));
                            jsonObject.put("WakeupTime",tags.get(3));
                            jsonObject.put("SleepTime",tags.get(4));
                            jsonObject.put("Snoring",tags.get(5));
                            jsonObject.put("Smoking",tags.get(6));
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        JsonParser jsonParser = new JsonParser();
                        //String recieve = Data.POST("ws://52.79.234.253",jsonObject);
                        //getData(new JSONArray(jsonParser.parse(recieve)));
                        filterDialog.dismiss();

                        filterDialog.dismiss();
                    }
                });

                filterDialog.findViewById(R.id.btn_filerClose).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        filterDialog.dismiss();
                    }
                });
            }
        });

    }



    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }
    private void getData() {

        //이름
        List<String> listTitle = Arrays.asList(
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
                "홍길동",
                "컴공 20학번",
                "고학번 취준생");

        //자기소개
        List<String> listContent = Arrays.asList(
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다"
        );

        //프로필 사진
        List<Integer> listResId = Arrays.asList(
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c);

        //리스트 목록만큼 출력합니다
        for (int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setName(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
    // 임의의 데이터 -> 최종적으로 DB에서 받을 수 있게 수정해야 함
    private void getData(JSONArray jsonArray) {

        List<String> listName = new ArrayList<>();
        List<String> listIntroduce = new ArrayList<>();
        List<Integer> listProfileImage = new ArrayList<>();

        for(int i = 0; i < jsonArray.length();i++)
        {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listName.add(jsonObject.get("Name").toString());
                listIntroduce.add(jsonObject.get("Introduce").toString());
                listProfileImage.add(Integer.getInteger(jsonObject.get("ProfileImage").toString()));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        //이름
        listName = Arrays.asList(
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
                "컴공 20학번",
                "고학번 취준생",
                "홍길동");

        //자기소개
        listIntroduce = Arrays.asList(
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.");

        //프로필 사진
        listProfileImage = Arrays.asList(
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c);

        //리스트 목록만큼 출력합니다
        for (int i = 0; i < listName.size(); i++) {
            Data data = new Data();
            data.setName(listName.get(i));
            data.setContent(listIntroduce.get(i));
            data.setResId(listProfileImage.get(i));
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }

    private JSONArray reqestProfiles(ArrayList<Integer> tags){
        // tags를 기반으로 프로필 요청을합니다.
        return null;
    }
    static String strJson = "";


}