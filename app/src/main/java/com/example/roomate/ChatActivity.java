package com.example.roomate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private MessageAdapter adapter;
    TextView receiveView;

    //보낼사람 받는사람?? 받아와야함
    private String Me = "1";
    private String You;

    @Override
    // 콜백 메소드, 생명주기에서 생성 단계에 한번 실행되는 메소드
    //파라미터는 activity의 이전 상태 저장
    protected void onCreate(Bundle savedInstanceState) {
        //super class 호출
        super.onCreate(savedInstanceState);

        //레이아웃 전달(layout에 있는 activity_main.xml로 화면 정의)
        setContentView(R.layout.activity_chat);


        //어댑터 클릭 이벤트시 받는 데이터
        Intent intent = getIntent();
        String receiveStr = intent.getExtras().getString("name");// 전달한 값을 받을 때
        String othersKakaoID = intent.getExtras().getString("KakaoID"); // 상대방 카카오아이디
        String myKakaoID = null;
        try {
            myKakaoID = Data.readMyInfo().getString("KakaoID");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Me = myKakaoID;
        You = othersKakaoID;
        try {
            myKakaoID = Data.readMyInfo().getString("KakaoID");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveView = (TextView)findViewById(R.id.chat_title);
        receiveView.setText(receiveStr);

        ImageView imageview = (ImageView)findViewById(R.id.chat_pr);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageview.setImageBitmap(bitmap);



        //listview -> 리스트 형태의 데이터를 보여주기 위한 위젯
        //list 형태의 원본 데이터가 있어야 하며 adapter를 통해 각각의 아이템에 지정해준다
        //어댑터는 데이터를 관리할 뿐만 아니라 뷰도 생성해줌

        //findViewById -> xml에서 우리가 지정해준 id로 위젯을 찾아 등록해줌
        ListView messageList = findViewById(R.id.messageList);
        EditText messageBox = findViewById(R.id.messageBox);
        TextView send = findViewById(R.id.send);

        instantiateWebSocket();

        //메세지 어뎁터 생성
        adapter = new MessageAdapter();
        messageList.setAdapter(adapter);

        //send버튼의 클릭 이벤트 처리
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                //메세지 박스의 getText() 하여 스트링으로 변환
                String message = messageBox.getText().toString();
                String sendMessage = Me +","+ You +","+ message;
                if(!message.isEmpty()){
                    webSocket.send(sendMessage);
                    messageBox.setText("");



                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", message);
                        jsonObject.put("byServer", false);

                        adapter.AddItem(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //뒤로가기: 상단 스택을 지우는 방식, 다른 방식으로 액티비티->플래그먼트 이동하면 오류가 자주 발생함
        ImageButton button_backchatroom = findViewById(R.id.btn_chatroom);
        button_backchatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); finish();
            }
        });


    }

    private void instantiateWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://52.79.234.253:8081").build();
        SocketListener socketListener = new SocketListener(this);
        webSocket = client.newWebSocket(request, socketListener);
        webSocket = client.newWebSocket(request, socketListener);
        //처음 웹소켓 통신시 "1", 자신, 상대 정보 전송 => 전에 보냈던 메세지들 모두 출력위함
        webSocket.send("load," + Me +","+ You);
    }

    public class SocketListener extends WebSocketListener {

        public ChatActivity activity;

        public SocketListener(ChatActivity activity){
            this.activity = activity;
        }
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Connection Established:", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //text들은 앞에 1, 0값중 하나 가짐 => 1이면 내가 보낸거, 0이면 상대가 보낸거
                        jsonObject.put("message", text.substring(1));
                        if(text.charAt(0) == '1')
                            jsonObject.put("byServer", false);
                        else
                            jsonObject.put("byServer", true);


                        adapter.AddItem(jsonObject);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
        }
    }

    public class  MessageAdapter extends BaseAdapter {

        List<JSONObject> messageList = new ArrayList<>();

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int i) {
            return messageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null)
                view = getLayoutInflater().inflate(R.layout.message_list_item, viewGroup, false);

            TextView sentMessage = view.findViewById(R.id.sentMessage);
            TextView receivedMessage = view.findViewById(R.id.receiveMessage);

            JSONObject item = messageList.get(i);

            try{
                if(item.getBoolean("byServer")){
                    receivedMessage.setVisibility(View.VISIBLE);
                    receivedMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);
                } else{
                    sentMessage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    receivedMessage.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e){
                e.printStackTrace();

            }

            return view;
        }
        void AddItem(JSONObject item) {
            messageList.add(item);
            notifyDataSetChanged();
        }
    }
}