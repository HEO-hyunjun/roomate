package com.example.roomate;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatroomScreen extends Fragment {

    private RecyclerView recyclerView;
    ChatroomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chatroom_screen, container, false);

        recyclerView =(RecyclerView) rootView.findViewById(R.id.testRec );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ChatroomAdapter();
        recyclerView.setAdapter(adapter);
        // init();//Recyclerview의 adapter 불러오기
        getProfiles();//Data 입력

        return rootView;
    }

    public void getProfiles(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/chatRoom.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //서버에서 요청한 자료가 응답으로 들어왔을때 코드입니다.
                            JSONObject jsonObjects = new JSONObject(response);
                            //adapter에 바로추가합니다.
                            for(int i = 0 ;i <jsonObjects.length()-2;i++) {
                                JSONObject jsonObject = new JSONObject(jsonObjects.getJSONObject(Integer.toString(i)).toString());
                                Data data = new Data();
                                data.setName(jsonObject.getString("Name"));
                                data.setContent(jsonObject.getString("Introduce"));
                                data.setResId(Data.parseIntToIconID(jsonObject.getInt("Profileimage")));
                                data.setID(jsonObject.getString("KakaoID"));
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
                Toast.makeText(getActivity().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
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
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 서버에 요청할때 입력값을 넣어줍니다.
                //본인 카카오아이디
                params.put("KakaoID", kakaoID);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}