package com.example.roomate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {

    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new BookmarkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.BookmarkHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다. Domitory에서 개수만큼 for문으로 출력시킵니다
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class BookmarkHolder extends RecyclerView.ViewHolder {

        private TextView textView1; // 이름
        private TextView textView2; // 자기소개
        private ImageView imageView; // 프로필사진

        BookmarkHolder(View item_View) {
            super(item_View);

            textView1 = item_View.findViewById(R.id.textView1);
            textView2 = item_View.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);

            //리스트 클릭 이벤트
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //이미지 불러오기
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    float scale = (float) (1024/(float)bitmap.getWidth());
                    int image_w = (int) (bitmap.getWidth() * scale);
                    int image_h = (int) (bitmap.getHeight() * scale);
                    Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                    resize.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    //이름, 이미지 전송
                    Intent intent = new Intent(view.getContext(), OtherProfile.class);
                    intent.putExtra("name", textView1.getText().toString());
                    intent.putExtra("image", byteArray);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);

                }
            });

        }

        void onBind(Data data) {
            textView1.setText(data.getName());
            textView2.setText(data.getContent());
            imageView.setImageResource(data.getResId());
        }
    }
}