package com.example.roomate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
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

        strMyName = "이름";
        strMyIntroduce = "자기소개를 작성해주세요";
        strMyGrade = "1학년";
        strMyDormitory ="3~5동";
        intMyProfileImage = 0;

        JSONObject myInfoRet = getMyprofile(strMyKakaoID);
        //grade, dom, gender, profileimage 가져오기
        try {
            myName.setText(myInfoRet.getString("Name"));
            myIntroduce.setText(myInfoRet.getString("Introduce"));
            myProfileImage.setImageResource(Data.parseIntToIconID(myInfoRet.getInt("Profileimage")));
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
        myProfileImage.setImageResource(Data.parseIntToIconID(intMyProfileImage));

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
                String strAge = spinner2.getSelectedItem().toString();
                if(strAge == "30+")
                    strAge = "31";
                int gender = 1;
                if(spinner3.getSelectedItem().toString() == "남자")
                    gender = 0;
                String grade = spinner1.getSelectedItem().toString();
                //서버에 post + info에 추가저장
                postMyprofile(new String[]{strMyIntroduce, strMyName, Integer.toString(profileId), strDormitory, strAge, Integer.toString(gender),grade}); // server에서 나의 정보를 갱신함.
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                String kakaoID = "1";
                // on below line we are passing our key and value pair to our parameters.
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.put("KakaoID", kakaoID);
                params.put("Introduce", input[0]);
                params.put("Name",input[1]);
                params.put("ProfileImage",input[2]);
                params.put("Dormitory",input[3]);
                params.put("Age",input[4]);
                params.put("Gender",input[5]);
                params.put("Grade",input[6]);
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
                return "application/x-www-form-urlencoded; charset=UTF-8";
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

}
