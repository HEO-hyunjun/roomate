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

    // 임의의 데이터 -> 최종적으로 DB에서 받을 수 있게 수정해야 함
    private void getData() {

        //이름
        List<String> listTitle = Arrays.asList(
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
                "컴공 20학번",
                "고학번 취준생",
                "홍길동",
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
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "함께 놀면서 친해질 룸메를 원합니다",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "코골이 안하는 비흡연자 룸메 구합니다.",
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다",
                "함께 놀면서 친해질 룸메를 원합니다");

        //프로필 사진
        List<Integer> listResId = Arrays.asList(
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
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
        for (int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setName(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
    public void getProfiles(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/filterp.php",
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
                String kakaoID = "10";
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