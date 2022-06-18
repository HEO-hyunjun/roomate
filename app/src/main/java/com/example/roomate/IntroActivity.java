package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro); // 띄울 화면 xml
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); // 넘겨줄 액티비티
                /*
                if (isLogin == true)
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                else
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                 */
                startActivity(intent);
                finish();
            }
        },2000); //화면 노출 시간
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
