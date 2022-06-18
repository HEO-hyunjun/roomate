package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InputProfileActivity extends AppCompatActivity {

    ImageButton backButton;
    Button saveButton;
    TextView tesss;
    ArrayList<Integer> tags = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);
        backButton = (ImageButton) findViewById(R.id.btn_backtohome);
        saveButton = (Button) findViewById(R.id.btn_save_my_input_profile);
        tesss = (TextView) findViewById(R.id.teeee);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str;
                Data forStringToTag = new Data();
                int flag = 1;

                RadioGroup personalityRGrp = (RadioGroup) findViewById(R.id.radio_personality);
                RadioButton personalityR = (RadioButton) findViewById(personalityRGrp.getCheckedRadioButtonId());
                str = personalityR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 0));

                RadioGroup hygieneRGrp = (RadioGroup) findViewById(R.id.radio_hygiene);
                RadioButton hygieneR = (RadioButton) findViewById(hygieneRGrp.getCheckedRadioButtonId());
                str = hygieneR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 1));

                RadioGroup noiseRGrp = (RadioGroup) findViewById(R.id.radio_noise);
                RadioButton noiseR = (RadioButton) findViewById(noiseRGrp.getCheckedRadioButtonId());
                str = noiseR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 2));

                RadioGroup wakeuptimeRGrp = (RadioGroup) findViewById(R.id.radio_wakeuptime);
                RadioButton wakeuptimeR = (RadioButton) findViewById(wakeuptimeRGrp.getCheckedRadioButtonId());
                str = wakeuptimeR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 3));

                RadioGroup sleeptimeRGrp = (RadioGroup) findViewById(R.id.radio_sleeptime);
                RadioButton sleeptimeR = (RadioButton) findViewById(sleeptimeRGrp.getCheckedRadioButtonId());
                str = sleeptimeR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 4));

                RadioGroup snoringRGrp = (RadioGroup) findViewById(R.id.radio_snoring);
                RadioButton snoringR = (RadioButton) findViewById(snoringRGrp.getCheckedRadioButtonId());
                str = snoringR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 5));

                RadioGroup smokingRGrp = (RadioGroup) findViewById(R.id.radio_smoking);
                RadioButton smokingR = (RadioButton) findViewById(smokingRGrp.getCheckedRadioButtonId());
                str = smokingR.getText().toString();
                tags.add(forStringToTag.parseTagToInt(str, 6));

                for(int i = 0 ; i < 7; i++) {
                    if (tags.get(i) == -1) {
                        flag = 0;
                    }
                }

                if(flag == 0) {
                    tesss.setText(tags.toString());
                    popup("주의", "모든 항목을 전부 채워주시기 바랍니다.");
                    tags.clear();
                }
                else {
                    finish();
                }
            }
        });
    }
    public void popup(String title, String contents){
        AlertDialog.Builder ad = new AlertDialog.Builder(InputProfileActivity.this);

        ad.setTitle(title);
        ad.setMessage(contents);
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ad.show();
    }
}