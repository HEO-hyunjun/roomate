package com.example.roomate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OtherProfile extends AppCompatActivity {

    TextView receive_name;
    boolean isAdded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otherprofile);

        Button btnAddORDelete = (Button) findViewById(R.id.addORdeleteBookmark);
        isAdded = false;
        //get isAdded 관심목록에 추가가 돼어있는지
        if(isAdded){
            btnAddORDelete.setText("관심목록에서 삭제하기");
            btnAddORDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //post 관심목록에서 삭제
                    isAdded = false;
                    btnAddORDelete.setText("관심목록에 추가하기");
                }
            });
        }
        else{
            btnAddORDelete.setText("관심목록에 추가하기");
            btnAddORDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //post 관심목록에 추가
                    isAdded = true;
                    btnAddORDelete.setText("관심목록에서 삭제하기");
                }
            });
        }

        //어댑터 클릭 이벤트시 받는 데이터
        Intent intent = getIntent();
        String receiveStr = intent.getExtras().getString("name");// 전달한 값을 받을 때
        receive_name = (TextView)findViewById(R.id.other_title);
        receive_name.setText(receiveStr);

        ImageView imageview = (ImageView)findViewById(R.id.other_pic);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageview.setImageBitmap(bitmap);

        //뒤로가기: 상단 스택을 지우는 방식, 다른 방식으로 액티비티->플래그먼트 이동하면 오류가 자주 발생함
        ImageButton button_backbook = findViewById(R.id.btn_book);
        button_backbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherProfile.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); finish();
            }
        });
    }

}
