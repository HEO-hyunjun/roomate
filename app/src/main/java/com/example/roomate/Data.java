/*
05.25 박우천
Data는 RecyclerAdaper 데이터를 보내는 역할입니다.
현재의 구성요소: 이름, 자기소개, 프로필 사진
추가할 구성요소: 성격유형
*/

package com.example.roomate;


import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Data extends AppCompatActivity {
    private final String URL = "http://52.79.234.253/Roommating/v1/Api.php?apicall=sort";
    private String ID;
    private String name; //이름
    private String introduce; //자기소개
    private int resId; //프로필 사진
    private int[] tag = new int[8];
    //InputStream inputStream = null;
    //AssetManager assetManager =getResources().getAssets();
    public static int parseIntToIconID(int num){
        int ret = 0;
        switch(num){
            case 0:
                ret = R.drawable.a;
                break;
            case 1:
                ret = R.drawable.b;
                break;
            case 2:
                ret = R.drawable.c;
                break;
        }
        return ret;
    }
    public static int parseIconIDTOInt(int num){
        int ret = R.drawable.a;
        switch(num){
            case R.drawable.a:
                ret = 0;
                break;
            case R.drawable.b:
                ret = 1;
                break;
            case R.drawable.c:
                ret = 2;
                break;
        }
        return ret;
    }

    public String parseTagToString(ArrayList<Integer> tagInput, int index) {
        // tagInput 태그 배열 그대로 들어옴
        // index 몇번째 태그정보인지
        String ret = "Error!!!";
        int tagInputData = tagInput.get(index);

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
                        ret = " 중간 ";
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
                        ret = " 흡연자 ";
                        break;
                    case 1:
                        ret = " 비흡연자 ";
                        break;
                }
                break;
        }
        return ret;
    }

    public int parseTagToInt(String tagInput, int index) {
        //tag의 정보를 string -> int로 바꿔줌
        int ret = 10;
        if (tagInput=="default")
            return ret;
        switch(index)
        {
            case 0:
                //성격유형
                switch (tagInput) {
                    case "매우 외향적":
                        ret = 0;
                        break;
                    case "조금 외향적":
                        ret = 1;
                        break;
                    case " 중간 ":
                        ret = 2;
                        break;
                    case "조금 내향적":
                        ret = 3;
                        break;
                    case "매우 내향적":
                        ret = 4;
                        break;
                }
                break;
            case 1:
                //청결
            case 2:
                //소음 민감도
                switch (tagInput) {
                    case "매우 예민함":
                        ret = 0;
                        break;
                    case "조금 예민함":
                        ret = 1;
                        break;
                    case "평범함":
                        ret = 2;
                        break;
                    case "조금 둔감함":
                        ret = 3;
                        break;
                    case "매우 둔감함":
                        ret = 4;
                        break;
                }
                break;
            case 3:
                //기상 시간
                switch (tagInput) {
                    case "7시 이전":
                        ret = 0;
                        break;
                    case "7~8시":
                        ret = 1;
                        break;
                    case "8~9시":
                        ret = 2;
                        break;
                    case "9~10시":
                        ret = 3;
                        break;
                    case "10시 이후":
                        ret = 4;
                        break;
                }
                break;
            case 4:
                //취침 시간
                switch (tagInput) {
                    case "22시 이전":
                        ret = 0;
                        break;
                    case "22~23시":
                        ret = 1;
                        break;
                    case "23~0시":
                        ret = 2;
                        break;
                    case "0~1시":
                        ret = 3;
                        break;
                    case "1시 이후":
                        ret = 4;
                        break;
                }
                break;
            case 5:
                //코골이
                switch (tagInput) {
                    case " 있다 ":
                        ret = 0;
                        break;
                    case " 없다 ":
                        ret = 1;
                        break;
                }
                break;
            case 6:
                //흡연여부
                switch (tagInput) {
                    case " 흡연자 ":
                        ret = 0;
                        break;

                    case " 비흡연자 ":
                        ret = 1;
                        break;
                }
                break;
            }
        return ret;
    }

    public String getID(){
        return ID;
    }
    public String getName() {
        //String ret = getJsonString(path, "name");
        return name;
    }

    public static JSONObject readMyInfo() throws IOException {
        String fileTitle = "/data/data/com.example.roomate/files/Userinfo.json";
        File file = new File(fileTitle);
        JsonParser jsonParser = new JsonParser();
        JSONObject ret = new JSONObject();

        if(file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                JSONTokener token = new JSONTokener(result);
                ret = new JSONObject(token);
                reader.close();
            } catch (FileNotFoundException e1) {

            } catch (IOException e2) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("readmyData!!",ret.toString());
        return ret;
    }

    public static void writeMyInfo(JSONObject input) throws IOException {
        String fileTitle = "/data/data/com.example.roomate/files/Userinfo.json";
        File file = new File(fileTitle);

        if(file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                Log.e("myResult",result);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                JSONObject jsonRes = new JSONObject();
                try {
                    JSONObject[] objs = new JSONObject[]{new JSONObject(result), input};
                    for (JSONObject obj : objs) {
                        Iterator it = obj.keys();
                        while (it.hasNext()) {
                            String key = (String)it.next();
                            jsonRes.put(key, obj.get(key));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                writer.write(jsonRes.toString());
                writer.flush();
                writer.close();
            } catch (FileNotFoundException e1) {

            } catch (IOException e2) {

            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setID(String ID) { this.ID=ID;}

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