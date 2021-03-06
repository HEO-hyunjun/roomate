package com.example.roomate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InputProfileActivity extends AppCompatActivity {

    ImageButton backButton;
    Button saveButton;
    ArrayList<Integer> tags = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);
        backButton = (ImageButton) findViewById(R.id.btn_backtohome);
        saveButton = (Button) findViewById(R.id.btn_save_my_input_profile);

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
                    if (tags.get(i)  == 10) {
                        flag = 0;
                    }
                }

                if(flag == 0) {
                    popup("??????", "?????? ????????? ?????? ??????????????? ????????????.");
                    tags.clear();
                }
                else {
                    pushMyProfile(tags);
                    JSONObject input = new JSONObject();
                    try {
                        input.put("Personality", tags.get(0));
                        input.put("Hygiene", tags.get(1));
                        input.put("Noise", tags.get(2));
                        input.put("WakeupTime",tags.get(3));
                        input.put("SleepTime",tags.get(4));
                        input.put("Snoring",tags.get(5));
                        input.put("Smoking",tags.get(6));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Data.writeMyInfo(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }
    public void popup(String title, String contents){
        AlertDialog.Builder ad = new AlertDialog.Builder(InputProfileActivity.this);

        ad.setTitle(title);
        ad.setMessage(contents);
        ad.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        ad.show();
    }
    public void pushMyProfile(ArrayList<Integer> input){
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST,"http://52.79.234.253/Roommating/v1/mydata.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //???????????? ????????? ????????? ???????????? ??????????????? ???????????????.
                            JSONObject jsonObjects = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // ??????????????? ???????????????.
                Toast.makeText(getApplication().getApplicationContext(), "Fail to get profiles" + error, Toast.LENGTH_SHORT).show();
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

                Map<String, String> params = new HashMap<String, String>();
                String kakaoID = "10";
                try {
                    kakaoID = Data.readMyInfo().getString("KakaoID");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // ????????? ???????????? ???????????? ???????????????.
                params.put("KakaoID", kakaoID);
                params.put("Personality",input.get(0).toString());
                params.put("Hygiene",input.get(1).toString());
                params.put("Noise",input.get(2).toString());
                params.put("WakeupTime",input.get(3).toString());
                params.put("SleepTime",input.get(4).toString());
                params.put("Snoring",input.get(5).toString());
                params.put("Smoking",input.get(6).toString());
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}