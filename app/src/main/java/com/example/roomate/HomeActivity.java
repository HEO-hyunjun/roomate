/*
05.25 박우천
HomeActivate로 홈 화면에서 3개의 기숙사를 클릭했을때 들어가지는 것 까지 구현했습니다.
 */

package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton button_Domitory_3to5 = findViewById(R.id.btn_dm_3to5);
        ImageButton button_Domitory_8 = findViewById(R.id.btn_dm_8);
        ImageButton button_Domitory_9 = findViewById(R.id.btn_dm_9);

        button_Domitory_3to5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Domitory_3to5.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        button_Domitory_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Domitory_8.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        button_Domitory_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Domitory_9.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });


    }
}