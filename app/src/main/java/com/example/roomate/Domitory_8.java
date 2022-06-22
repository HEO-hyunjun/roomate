package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.WebSocket;

public class Domitory_8 extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;
    private RecyclerAdapter adapter;

    Dialog filterDialog;

    ArrayList<Integer> tags = new ArrayList<>();
    private WebSocket webSocket;
    private ChatActivity.MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity간 자료공유코드

        Intent secondIntent = getIntent();
        String test = secondIntent.getStringExtra("test");
        setContentView(R.layout.activity_domitory8);

        TextView testView = findViewById(R.id.forTesttext);



        ImageButton button_backHome = findViewById(R.id.btn_backtohome); // 뒤로가기 버튼 선언
        ImageButton button_filter = findViewById(R.id.btn_filter);


        filterDialog = new Dialog(Domitory_8.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        filterDialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        filterDialog.setContentView(R.layout.filter_dialog);


        init();//Recyclerview의 adapter 불러오기
        getData();//Data 입력

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

                        ImageButton EndDialog = (ImageButton)filterDialog.findViewById( (R.id.btn_filerClose) );

                        EndDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                filterDialog.cancel();
                            }
                        });

                        tags.add(forChipToTag.parseTagToInt(personalityCh.getText().toString(), 0));
                        tags.add(forChipToTag.parseTagToInt(wakeuptimeCh.getText().toString(), 3));
                        tags.add(forChipToTag.parseTagToInt(sleeptimeCh.getText().toString(), 4));
                        tags.add(forChipToTag.parseTagToInt(snoringCh.getText().toString(), 5));
                        tags.add(forChipToTag.parseTagToInt(noiseCh.getText().toString(), 2));
                        tags.add(forChipToTag.parseTagToInt(hygieneCh.getText().toString(), 1));
                        tags.add(forChipToTag.parseTagToInt(smokingCh.getText().toString(), 6));
                        testView.setText(tags.toString());
                        //post tags
                        //String message = tags.toString();
                        tags.clear();
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
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    // 임의의 데이터 -> 최종적으로 DB에서 받을 수 있게 수정해야 함
    private void getData() {
//
//        //이름
//        List<String> listTitle = Arrays.asList(
//                "홍길동",
//                "컴공 20학번",
//                "고학번 취준생",
//                "홍길동",
//                "컴공 20학번",
//                "고학번 취준생");
//
//        //자기소개
//        List<String> listContent = Arrays.asList(
//                "함께 놀면서 친해질 룸메를 원합니다",
//                "코골이 안하는 비흡연자 룸메 구합니다.",
//                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
//                "함께 놀면서 친해질 룸메를 원합니다",
//                "코골이 안하는 비흡연자 룸메 구합니다.",
//                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다"
//                );
//
//        //프로필 사진
//        List<Integer> listResId = Arrays.asList(
//                R.drawable.a,
//                R.drawable.b,
//                R.drawable.c,
//                R.drawable.a,
//                R.drawable.b,
//                R.drawable.c);
//
//        //리스트 목록만큼 출력합니다
//        for (int i = 0; i < listTitle.size(); i++) {
//            Data data = new Data();
//            data.setName(listTitle.get(i));
//            data.setContent(listContent.get(i));
//            data.setResId(listResId.get(i));
//            adapter.addItem(data);
//        }
//
//        adapter.notifyDataSetChanged();
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
                String kakaoID = "10";
                String dormitory = "asd";
                int gender = 0;
                /*try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                    gender = Data.readMyInfo().getInt("gender");
                    dormitory = Data.readMyInfo().getString("Dormitory");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

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
    public void getSimilar(ArrayList<Integer> input){
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/sort.php ",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // on below line passing our response to json object.
                            JSONObject jsonObject = new JSONObject(response);

                            Log.e("eefsdfee",jsonObject.getString("Name"));
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
                String dormitory = "8동";
                int gender = 0;
                // on below line we are passing our key and value pair to our parameters.
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                    gender = Data.readMyInfo().getInt("gender");
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