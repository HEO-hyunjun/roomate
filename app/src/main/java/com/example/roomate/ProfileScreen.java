package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 *
 *     프로필 추가 버튼을 만들어 놓고,
 *     나의 프로필의 경우, 버튼을 안보이게
 *     상대방의 프로필의 경우, 이미 관심목록에 존재하는지 확인하고
 *     관심목록에 있을경우, 색을 바꿔서 관심목록에서 제거 버튼으로 바꾸기
 *
 */
public class ProfileScreen extends Fragment {

    //드롭다운 스피너 선언 및 스트링 값
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private static final String[] grade = new String[]{"1학년","2학년","3학년","4학년"};
    private static final String[] age = new String[]{"18","19","20","21","22","23","24","25","26","27","28","29","30+"};
    private static final String[] gender = new String[]{"남자","여자"};
    private static final String[] domitory_number = new String[]{"3~5동","8동","9동"};


    ProfileScreen(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        ImageButton btnInputMyProfile = (ImageButton) v.findViewById(R.id.btn_my_input_profile);


        //스피너
        spinner1= (Spinner)v.findViewById(R.id.sp_grade);
        spinner2= (Spinner)v.findViewById(R.id.sp_age);
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

        //나이 18~30+
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,age);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        //성격유형 검사로 들어가는 버튼
        btnInputMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InputProfileActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

}
