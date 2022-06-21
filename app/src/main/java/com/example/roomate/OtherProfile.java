package com.example.roomate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OtherProfile extends AppCompatActivity {

    TextView receive_name;
    TextView recieve_nickname ;
    TextView recieve_grade;
    TextView receive_profile;

    String movieName1, movieName2;
    String rank1, rank2;

    static RequestQueue requestQueue;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otherprofile);

        //어댑터 클릭 이벤트시 받는 데이터
        Intent intent = getIntent();
        String receiveStr = intent.getExtras().getString("name");// 전달한 값을 받을 때
        receive_name = (TextView)findViewById(R.id.other_title);
        receive_name.setText(receiveStr);

        ImageView imageview = (ImageView)findViewById(R.id.other_pic);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageview.setImageBitmap(bitmap);

        //뒤로가기: 상단 스택을 지우는 방식, 다른 방식으로 액티비티->플래그먼트 이동하면 오류가 자주 발생함
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

        recieve_nickname = (TextView)findViewById(R.id.other_name);
        recieve_grade = (TextView)findViewById(R.id.other_grade);
        receive_profile = (TextView)findViewById(R.id.other_intro);



        //내부 json파일 만들어서 데이터 업데이트 시도
        //프로필 이름과 학년에 설정된 값이 나옴
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
//구현중
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
//                //응답이 되었을때 response로 값이 들어옴
//                //Toast.makeText(getApplicationContext(), "응답:" + response, Toast.LENGTH_SHORT).show();
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
//                //에러나면 error로 나옴
//                Toast.makeText(getApplicationContext(), "에러:" + error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> param = new HashMap<String, String>();
//                //php로 설정값을 보낼 수 있음
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
}
