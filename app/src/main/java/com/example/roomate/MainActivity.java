/*
05.25 박우천
구성상 MainActivitiy를 로그인 페이지로 만들었습니다.
Kakao API 사용하기 전에 시험용으로 버튼을 눌렀을때 홈 화면으로 전환하는 것까지만 구현했습니다.

Fragment만을 이용해서 구성하기가 생각보다 어렵습니다. 그래서 비교적 쉬운 Activitiy 간 이동으로 구성했습니다.
Fragment는 홈 화면부터 나타나는 하단 네비게이터를 사용할때 적용하는 게 좋을 것 같다는 의견입니다.

참고로 버튼은 ImageButton을 사용했습니다. 이를 이용하려면 버튼 선언 시 ImageButton으로 해야합니다.

*/

package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button_login = findViewById(R.id.btn_login); // 이미지 버튼 선언

        //OnClickListener를 이용해서 이미지 버튼을 눌렀을 때 HomeActivitiy로 넘어갑니다
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
    }
}