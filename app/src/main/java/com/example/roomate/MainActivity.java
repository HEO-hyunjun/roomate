/*
05.25 박우천
구성상 MainActivitiy를 로그인 페이지로 만들었습니다.
Kakao API 사용하기 전에 시험용으로 버튼을 눌렀을때 홈 화면으로 전환하는 것까지만 구현했습니다.

Fragment만을 이용해서 구성하기가 생각보다 어렵습니다. 그래서 비교적 쉬운 Activitiy 간 이동으로 구성했습니다.
Fragment는 홈 화면부터 나타나는 하단 네비게이터를 사용할때 적용하는 게 좋을 것 같다는 의견입니다.

참고로 버튼은 ImageButton을 사용했습니다. 이를 이용하려면 버튼 선언 시 ImageButton으로 해야합니다.

*/

package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("KeyHash", getKeyHash());

        ImageButton button_login = findViewById(R.id.btn_login); // 이미지 버튼 선언

        //OnClickListener를 이용해서 이미지 버튼을 눌렀을 때 HomeActivitiy로 넘어갑니다
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    login();
                }
                else{
                    accountLogin()                    
                }
                
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });
    }

    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void getUserInfo(){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: "+user.getId() +
                            "\n닉네임  : "+user.getKakaoAccount().getProfile().getNickname());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);
                if(! new File("/data/data/com.example.roomate/files/Userinfo.json").exists()){
                    JsonString jsonString = new JsonString();
                    jsonString.setKakaoid(user.getId());
                    jsonString.setNickname(user.getKakaoAccount().getProfile().getNickname());


                    String filename = "Userinfo.json";
                    FileOutputStream outputStream;

                    try {

                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(JsonUtil.toJSon(jsonString).getBytes());
                        outputStream.close();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }
            return null;
        });
    }

    // 키해시 얻는 코드
    public String getKeyHash(){
        try{
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if(packageInfo == null) return null;
            for(Signature signature: packageInfo.signatures){
                try{
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                }catch (NoSuchAlgorithmException e){
                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
                }
            }
        }catch(PackageManager.NameNotFoundException e){
            Log.w("getPackageInfo", "Unable to getPackageInfo");
        }
        return null;
    }

}
