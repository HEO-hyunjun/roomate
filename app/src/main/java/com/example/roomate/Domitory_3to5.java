/*
05.25 박우천
Domitory_3to5는 기숙사 3~5동 목록입니다. 다른 8동 9동은 변수명 빼고는 코드가 동일합니다.
뒤로가기와 Recyclerview로 목록 생성이 되는 것까지 구현했습니다. 이후에 DB를 통해 가져오도록 수정해야 합니다.
 */

package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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
        getSimilar();//Data 불러오기
        //getData(myTags);
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
                        tags.clear();
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
                        getData(tags);
                        filterDialog.dismiss();


                        filterDialog.dismiss();
                    }
                });

                filterDialog.findViewById(R.id.btn_filerClose ).setOnClickListener( new View.OnClickListener(){
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
    // 임의의 데이터 -> 최종적으로 DB에서 받을 수 있게 수정해야 함
    private void getData(ArrayList<Integer> input) {
        adapter.notifyDataSetChanged();
        getProfiles(input);
    }

    public void getProfiles(ArrayList<Integer> input){
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/filterp.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //서버에서 요청한 자료가 응답으로 들어왔을때 코드입니다.
                            JSONObject jsonObjects = new JSONObject(response);
                            Log.e("eefsdfee",Integer.toString(jsonObjects.length()));
                            //adapter에 바로추가합니다.
                            for(int i = 0 ;i <jsonObjects.length()-2;i++) {
                                JSONObject jsonObject = new JSONObject(jsonObjects.getJSONObject(Integer.toString(i)).toString());
                                Data data = new Data();
                                data.setName(jsonObject.getString("Name"));
                                data.setContent(jsonObject.getString("Introduce"));
                                data.setResId(Data.parseIntToIconID(jsonObject.getInt("Profileimage")));
                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 오류발생시 코드입니다.
                Toast.makeText(getApplication().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                String kakaoID = "1";
                String dormitory = "35동";
                int gender = 0;
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                    gender = Data.readMyInfo().getInt("Gender");
                    dormitory = Data.readMyInfo().getString("Dormitory");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("filter",input.toString());
                // 서버에 요청할때 입력값을 넣어줍니다.
                params.put("KakaoID", kakaoID);
                params.put("Gender", Integer.toString(gender));
                params.put("Dormitory", dormitory);
                params.put("Personality",input.get(0).toString());
                params.put("Hygiene",input.get(1).toString());
                params.put("Noise",input.get(2).toString());
                params.put("WakeupTime",input.get(3).toString());
                params.put("SleepTime",input.get(4).toString());
                params.put("Snoring",input.get(5).toString());
                params.put("Smoking",input.get(6).toString());
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    public void getSimilar(){
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/sort.php ",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // on below line passing our response to json object.
                            JSONObject jsonObject = new JSONObject(response);

                            // on below line we are checking if the response is null or not.
                            Data data = new Data();
                            data.setName(jsonObject.getString("Name"));
                            data.setContent(jsonObject.getString("Introduce"));
                            data.setResId(Data.parseIntToIconID(jsonObject.getInt("Profileimage")));
                            adapter.addItem(data);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getApplication().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                String kakaoID = "1";
                String dormitory = "35동";
                int gender = 0;
                // on below line we are passing our key and value pair to our parameters.
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                    gender = Data.readMyInfo().getInt("Gender");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.put("KakaoID", kakaoID);
                params.put("Gender", Integer.toString(gender));
                params.put("Dormitory", dormitory);
                return params;
            }
        };

        queue.add(request);
    }
}