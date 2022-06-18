/*
05.25 박우천
Data는 RecyclerAdaper 데이터를 보내는 역할입니다.
현재의 구성요소: 이름, 자기소개, 프로필 사진
추가할 구성요소: 성격유형
*/

package com.example.roomate;


import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data extends AppCompatActivity {

    private String ID;
    private String name; //이름
    private String introduce; //자기소개
    private int resId; //프로필 사진
    private int[] tag = new int[8];
    //InputStream inputStream = null;
    //AssetManager assetManager =getResources().getAssets();


    /*
    public String getJsonString(String path, String parameter){
        String ret = "Error!!!";
        try{
            inputStream = assetManager.open(path, AssetManager.ACCESS_BUFFER);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            //json 파일 불러들여 string jsonData에 넣음
            String jsonData = buffer.toString();

            JSONObject jsonObject = null;

            //array에서 불러올때
            try {
                jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                JSONObject jo = jsonArray.getJSONObject(0);

                ret = jo.getString(parameter);
            } catch (JSONException e) {e.printStackTrace();}

        } catch(IOException e){e.printStackTrace();}

        return ret;
    }

    public String parseTag(int[] tagInput, int index) {
        // tagInput 태그 배열 그대로 들어옴
        // index 몇번째 태그정보인지

        String ret = "Error!!!";
        int tagInputData = tagInput[index];

        switch(index)
        {
            case 0:
                //성격유형
                switch(tagInputData)
                {
                    case 0:
                        ret = "매우 외향적";
                        break;
                    case 1:
                        ret = "조금 외향적";
                        break;
                    case 2:
                        ret = "중간";
                        break;
                    case 3:
                        ret = "조금 내향적";
                        break;
                    case 4:
                        ret = "매우 내향적";
                        break;
                }
                break;
            case 1:
                //청결
            case 2:
                //소음 민감도
                switch(tagInputData)
                {
                    case 0:
                        ret = "매우 예민함";
                        break;
                    case 1:
                        ret = "조금 예민함";
                        break;
                    case 2:
                        ret = "평범함";
                        break;
                    case 3:
                        ret = "조금 둔감함";
                        break;
                    case 4:
                        ret = "매우 둔감함";
                        break;
                }
                break;
            case 3:
                //기상 시간
                switch(tagInputData)
                {
                    case 0:
                        ret = "7시 이전";
                        break;
                    case 1:
                        ret = "7~8시";
                        break;
                    case 2:
                        ret = "8~9시";
                        break;
                    case 3:
                        ret = "9~10시";
                        break;
                    case 4:
                        ret = "10시 이후";
                        break;
                }
                break;
            case 4:
                //취침 시간
                switch(tagInputData)
                {
                    case 0:
                        ret = "22시 이전";
                        break;
                    case 1:
                        ret = "22~23시";
                        break;
                    case 2:
                        ret = "23~0시";
                        break;
                    case 3:
                        ret = "0~1시";
                        break;
                    case 4:
                        ret = "1시 이후";
                        break;
                }
                break;
            case 5:
                //코골이
                switch(tagInputData)
                {
                    case 0:
                        ret = "있다";
                        break;
                    case 1:
                        ret = "없다";
                        break;
                }
                break;
            case 6:
                //흡연여부
                switch(tagInputData)
                {
                    case 0:
                        ret = "흡연자";
                        break;
                    case 1:
                        ret = "비흡연자";
                        break;
                }
                break;
            case 7:
                //언어
                switch(tagInputData)
                {
                    case 0:
                        ret = "한국어";
                        break;
                    case 1:
                        ret = "외국어";
                        break;
                }
                break;
        }
        return ret;
    }

    public int[] getTag(String path, int index){
        ArrayList<Integer> tags = new ArrayList<>();
        try{
            inputStream = assetManager.open(path, AssetManager.ACCESS_BUFFER);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line != null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            //json 파일 불러들여 string jsonData에 넣음
            String jsonData = buffer.toString();

            JSONObject jsonObject = null;

            //object 하나만 불러올때
            try {
                //json파일 분석후
                jsonObject = new JSONObject(jsonData);
                tags.add(jsonObject.getInt("Personality"));
                tags.add(jsonObject.getInt("Hygiene"));
                tags.add(jsonObject.getInt("Noise"));
                tags.add(jsonObject.getInt("WakeupTime"));
                tags.add(jsonObject.getInt("SleepTime"));
                tags.add(jsonObject.getInt("Snoring"));
                tags.add(jsonObject.getInt("Smoking"));
                tags.add(jsonObject.getInt("Language"));
            } catch (JSONException e) {e.printStackTrace();}

        } catch(IOException e){e.printStackTrace();}
        return tag;
    }

     */

    public String getID(){
        return ID;
    }
    public String getName() {
        //String ret = getJsonString(path, "name");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return introduce;
    }

    public void setContent(String introduce) {
        this.introduce = introduce;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}