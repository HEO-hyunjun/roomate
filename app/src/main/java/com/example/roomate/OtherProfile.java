package com.example.roomate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OtherProfile extends AppCompatActivity {

    TextView recieve_nickname ;
    TextView recieve_grade;
    TextView receive_profile;
    Button addOrdelete;

    String strMyKakaoID;
    String strKakaoID;
    String movieName1, movieName2;
    String rank1, rank2;

    static RequestQueue requestQueue;

    Gson gson = new Gson();
    boolean isAdded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otherprofile);
        strMyKakaoID = "1";
        try {
            JSONObject jsonObject = Data.readMyInfo();
            strMyKakaoID = jsonObject.getString("KakaoID");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Button btnAddORDelete = (Button) findViewById(R.id.addORdeleteBookmark);
        Intent intent = new Intent(this, OtherProfile.class);
        recieve_nickname = (TextView)findViewById(R.id.other_name);
        recieve_grade = (TextView)findViewById(R.id.other_grade);
        receive_profile = (TextView)findViewById(R.id.other_intro);
        strKakaoID = intent.getStringExtra("KakaoID");

        btnAddORDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdded = isMatched();
                if(isAdded){
                    isAdded = false;
                    btnAddORDelete.setText("??????????????? ????????????");
                    deleteThisprofile();
                }
                else{
                    //post ??????????????? ??????
                    isAdded = true;
                    btnAddORDelete.setText("?????????????????? ????????????");
                    addThisprofile();
                }
            }
        });
        //get isAdded ??????????????? ????????? ???????????????


        //????????????: ?????? ????????? ????????? ??????, ?????? ???????????? ????????????->??????????????? ???????????? ????????? ?????? ?????????
        ImageButton button_backbook = findViewById(R.id.btn_book);
        button_backbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherProfile.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); finish();
            }
        });

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }



        //?????? json?????? ???????????? ????????? ???????????? ??????
        //????????? ????????? ????????? ????????? ?????? ??????
        try {
            InputStream is = getAssets().open("jsonData.json");
            byte[]buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);

            Map<String,Object> boxOfficeResult= gson.fromJson( jsonObject.get("boxOfficeResult").toString(),new TypeToken<Map<String, Object>>(){}.getType());

            ArrayList<Map<String, Object>> jsonList = (ArrayList) boxOfficeResult.get("dailyBoxOfficeList");

            movieName1=jsonList.get(0).get("movieNm").toString();
            rank1=jsonList.get(0).get("rank").toString();
            movieName2=jsonList.get(1).get("movieNm").toString();
            rank2=jsonList.get(1).get("rank").toString();

        } catch (Exception e) {e.printStackTrace();
        }


        //getProfile();

        action();
    }
//?????????
//    public void getProfile() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        String URL = "http://api.androidhive.info/volley/person_object.json";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
//                new Response.Listener<jsonobject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                //????????? ???????????? response??? ?????? ?????????
//                //Toast.makeText(getApplicationContext(), "??????:" + response, Toast.LENGTH_SHORT).show();
//                try {
////
//                    String id = response.getString("name");
//                    String recordDate = response.getString("email");
//                    JSONObject distance = response.getJSONObject("phone");
//
//                } catch (Exception e) {e.printStackTrace();
//
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //???????????? error??? ??????
//                Toast.makeText(getApplicationContext(), "??????:" + error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<String, String>();
//                //php??? ???????????? ?????? ??? ??????
//                return param;
//            }
//        };
//
//
//        request.setShouldCache(false);
//        requestQueue.add(request);
//    }


    public void action(){
        recieve_nickname.setText(movieName1);
        recieve_grade.setText(rank1);
    }

    public void addThisprofile(){
        RequestQueue queue = Volley.newRequestQueue(OtherProfile.this);

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/myprofile.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // on below line passing our response to json object.
                            JSONObject jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OtherProfile.this, "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8-sig";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                // on below line we are passing our key and value pair to our parameters.
                params.put("KakaoID1", strMyKakaoID);
                params.put("KakaoID2", strKakaoID);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    public void deleteThisprofile(){
        RequestQueue queue = Volley.newRequestQueue(OtherProfile.this);

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/chatRoomDel.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // on below line passing our response to json object.
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(OtherProfile.this, "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8-sig";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                // on below line we are passing our key and value pair to our parameters.
                params.put("KakaoID1", strMyKakaoID);
                params.put("KakaoID2", strKakaoID);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    public boolean isMatched() {
        return true;
    }
}
