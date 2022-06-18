package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class Domitory_8 extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;
    private RecyclerAdapter adapter;

    Dialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domitory8);
        //테스트 남깁니다.
        //마지막입니다.
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

                        ChipGroup chgrp = (ChipGroup)filterDialog.findViewById((R.id.firstChipgroup));

                        Chip c = (Chip)filterDialog.findViewById(chgrp.getCheckedChipId());


                        String s = c.getText().toString();
                        //testText.setText(s);

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

        //이름
        List<String> listTitle = Arrays.asList(
                "홍길동",
                "컴공 20학번",
                "고학번 취준생",
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
                "서로 공부에만 집중할 수 있게 공부하는 룸메이트 구합니다"
                );

        //프로필 사진
        List<Integer> listResId = Arrays.asList(
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c);

        //리스트 목록만큼 출력합니다
        for (int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
}