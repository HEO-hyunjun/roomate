package com.example.roomate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfileScreen extends Fragment {

    //드롭다운 스피너 선언 및 스트링 값
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private static final String[] grade = new String[]{"1학년","2학년","3학년","4학년"};
    private static final String[] gender = new String[]{"남자","여자"};
    private static final String[] domitory_number = new String[]{"3~5동","8동","9동"};

    EditText myIntroduce;
    EditText myName;
    ImageButton myProfileImage;
    String strMyName ;
    String strMyIntroduce;
    String strMyGrade;
    String strMyDormitory;
    String strMyKakaoID;
    Integer intMyProfileImage;

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
        myIntroduce = (EditText) v.findViewById(R.id.myIntroduce);
        myName = (EditText) v.findViewById(R.id.myName);
        myProfileImage = (ImageButton) v.findViewById(R.id.profile_myprofile);
        Spinner sp_grade = (Spinner) v.findViewById(R.id.sp_grade);
        Spinner sp_dom = (Spinner) v.findViewById(R.id.sp_dom);
        Spinner sp_gender = (Spinner) v.findViewById(R.id.sp_gender);

        myProfileImage.setImageResource(R.drawable.a);
        myProfileImage.setTag(R.drawable.a);
        strMyName = "이름";
        strMyIntroduce = "자기소개를 작성해주세요";
        strMyGrade = "1학년";
        strMyDormitory ="3~5동";
        intMyProfileImage = 0;

        JSONObject myInfoRet = getMyprofile(strMyKakaoID);
        //grade, dom, gender, profileimage 가져오기
        try {
            Log.e("myInfoRet",myInfoRet.toString());
            myName.setText(myInfoRet.getString("Name"));
            myIntroduce.setText(myInfoRet.getString("Introduce"));
            myProfileImage.setImageResource(Data.parseIconIDTOInt(myInfoRet.getInt("Profileimage")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject myInfo = Data.readMyInfo();
            strMyName=myInfo.getString("Nickname");
            strMyIntroduce = myInfo.getString("Introduce");
            intMyProfileImage=Data.parseIntToIconID(myInfo.getInt("Profileimage"));
            strMyKakaoID = myInfo.getString("KakaoID");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject myInfo = Data.readMyInfo();
            strMyKakaoID = myInfo.getString("KakaoID");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }



        myName.setText(strMyName);
        myIntroduce.setText(strMyIntroduce);
        myProfileImage.setImageResource(intMyProfileImage);

        //스피너
        spinner1= (Spinner)v.findViewById(R.id.sp_grade);
        spinner3= (Spinner)v.findViewById(R.id.sp_gender);
        spinner4= (Spinner)v.findViewById(R.id.sp_dom);

        //학년 1~4학년
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,grade);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {@Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
        {
            Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT);
        }@Override
        public void onNothingSelected(AdapterView<?> adapterView) {}});

        //성별 남자,여자
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,gender);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {@Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
        {
            Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT);
        }@Override
        public void onNothingSelected(AdapterView<?> adapterView) {}});

        //기숙사 동 3~5동,8동,9동
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,domitory_number);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {@Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
        {
            Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT);
        }@Override
        public void onNothingSelected(AdapterView<?> adapterView) {}});




        ////spinner 고정
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object obj = parent.getItemAtPosition(pos);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("object", obj.toString());
                prefsEditor.commit();
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object obj = parent.getItemAtPosition(pos);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("object", obj.toString());
                prefsEditor.commit();
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object obj = parent.getItemAtPosition(pos);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString("object", obj.toString());
                prefsEditor.commit();
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //성격유형 검사로 들어가는 버튼
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
                        myProfileImage.setTag(R.drawable.a);
                        profileSelect.dismiss();
                    }
                });
                profileSelect.findViewById(R.id.profileImage2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myProfileImage.setImageResource(R.drawable.b);
                        myProfileImage.setTag(R.drawable.b);
                        profileSelect.dismiss();
                    }
                });
                profileSelect.findViewById(R.id.profileImage3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myProfileImage.setImageResource(R.drawable.c);
                        myProfileImage.setTag(R.drawable.c);
                        profileSelect.dismiss();
                    }
                });
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMyIntroduce = myIntroduce.getText().toString();
                String strMyName = myName.getText().toString();
                int profileId = Data.parseIconIDTOInt(new Integer(myProfileImage.getTag().toString()));
                String strDormitory = spinner4.getSelectedItem().toString();
                if(strDormitory =="3~5동")
                    strDormitory="35동";
                int gender = 1;
                if(spinner3.getSelectedItem().toString() == "남자")
                    gender = 0;
                String grade = spinner1.getSelectedItem().toString();
                //서버에 post + info에 추가저장
                //postDB(strMyKakaoID,strMyIntroduce,strMyName,Integer.toString(profileId),strDormitory,Integer.toString(gender),grade);
                postMyprofile(new String[]{strMyIntroduce, strMyName, Integer.toString(profileId), strDormitory, Integer.toString(gender),grade}); // server에서 나의 정보를 갱신함.
                JSONObject input = new JSONObject();
                try {
                    input.put("Grade", grade);
                    input.put("Gender",gender);
                    input.put("Dormitory",strDormitory);
                    input.put("Introduce",strMyIntroduce);
                    input.put("Profileimage",profileId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Data.writeMyInfo(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }
    public void postMyprofile(String[] input){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
                Toast.makeText(getActivity().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
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
                params.put("KakaoID", strMyKakaoID);
                params.put("Introduce", input[0]);
                params.put("Name",input[1]);
                params.put("Profileimage",input[2]);
                params.put("Dormitory",input[3]);
                params.put("Gender",input[4]);
                params.put("Grade",input[5]);
                //strMyIntroduce, strMyName, Integer.toString(profileId), strDormitory, Integer.toString(gender),grade
                Log.e("tags",params.toString());
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    public JSONObject getMyprofile(String kakaoID){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JSONObject ret = new JSONObject();
        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/readprofile.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("teet",jsonObject.toString());
                            //age, grade, dom, gender, profileimage 가져오기
                            ret.put("Grade",jsonObject.getString("Grade"));
                            ret.put("Profileimage",Data.parseIconIDTOInt(jsonObject.getInt("Profileimage")));
                            ret.put("Name",jsonObject.getString("Name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getActivity().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
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

                params.put("KakaoID", strMyKakaoID);
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
        return ret;
    }

    public void postDB(String strMyKakaoID, String strIntroduce, String strName, String strProfileimage, String strDormitory,String strGender,String strGrade){

        class joinHTTPt extends AsyncTask<Void, Void, Void> {

            String strMyKakaoID;
            String strIntroduce;
            String strProfileimage;
            String strDormitory;
            String strGender;
            String strGrade;
            String strName;

            public joinHTTPt(String strMyKakaoID, String strIntroduce,String strName, String strProfileimage, String strDormitory,String strGender,String strGrade){
                this.strMyKakaoID = strMyKakaoID;
                this.strIntroduce = strIntroduce;
                this.strProfileimage = strProfileimage;
                this.strDormitory = strDormitory;
                this.strGender = strGender;
                this.strGrade = strGrade;
                this.strName = strName;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    //--------------------------
                    //   URL 설정하고 접속하기
                    //--------------------------


                    URL url = new URL("http://52.79.234.253/Roommating/v1/myprofile.php");
                    HttpURLConnection huc = (HttpURLConnection) url.openConnection();


                    //--------------------------
                    //   전송 모드 설정 - 기본적인 설정이다
                    //--------------------------


                    huc.setDefaultUseCaches(false);
                    huc.setDoInput(true);                         // 서버에서 읽기 모드 지정
                    huc.setDoOutput(true);                       // 서버로 쓰기 모드 지정
                    huc.setRequestMethod("POST");         // 전송 방식은 POST

                    huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");



                    // 서버로 값 전달.
                    //--------------------------
                    //   서버로 값 전송
                    //--------------------------



                    StringBuffer buffer = new StringBuffer();
                    buffer.append("KakaoID").append("=").append(strMyKakaoID).append("&");                 // php 변수에 값 대입
                    buffer.append("Introduce").append("=").append(strIntroduce).append("&");
                    buffer.append("Name").append("=").append(strName).append("&");   // php 변수 앞에 '$' 붙이지 않는다
                    buffer.append("Profileimage").append("=").append(strProfileimage).append("&");           // 변수 구분은 '&' 사용
                    buffer.append("Dormitory").append("=").append(strDormitory).append("&");
                    buffer.append("Grade").append("=").append(strGrade).append("&");
                    buffer.append("Gender").append("=").append(strGender);


                    OutputStreamWriter outStream = new OutputStreamWriter(huc.getOutputStream(), "UTF-8");  // 안드에서 php로 보낼때 UTF8로 해야지 한글이 안깨진다.
//                    OutputStreamWriter outStream = new OutputStreamWriter(huc.getOutputStream(), "EUC-KR");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(buffer.toString());
                    writer.flush();


                    //--------------------------
                    //   서버에서 전송받기
                    //--------------------------
                    InputStreamReader tmp = new InputStreamReader(huc.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                        builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                    }
                    String myResult = builder.toString();                       // 전송결과를 전역 변수에 저장





                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        joinHTTPt gotoDBUerId = new joinHTTPt( strMyKakaoID,  strIntroduce, strName,  strProfileimage,  strDormitory, strGender, strGrade);
        gotoDBUerId.execute();

    }
}
