/*
05.25 박우천
HomeActivate로 홈 화면에서 3개의 기숙사를 클릭했을때 들어가지는 것 까지 구현했습니다.
 */

package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    HomeScreen homeScreen;
    ProfileScreen profileScreen;
    BookmarkScreen bookmarkScreen;
    ChatroomScreen chatroomScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Fragment 선언
        homeScreen = new HomeScreen();
        profileScreen = new ProfileScreen();
        bookmarkScreen = new BookmarkScreen();
        chatroomScreen = new ChatroomScreen();
        //홈스크린을 기본화면으로 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeScreen).commitAllowingStateLoss();

        BottomNavigationView navigationBar = findViewById(R.id.bottom_navigationview);
        navigationBar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){
                    // 홈 페이지로 이동
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeScreen).commit();
                        return true;
                    // 본인 프로필 페이지로 이동
                    case R.id.action_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, profileScreen).commit();
                        return true;
                    // 관심목록 페이지로 이동
                    case R.id.action_bookmark:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, bookmarkScreen).commit();
                        return true;
                    // 채팅 페이지로 이동
                    case R.id.action_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, chatroomScreen).commit();
                        return true;
                }
                return false;
            }
        });
    }
}