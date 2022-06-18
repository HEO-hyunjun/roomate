package com.example.roomate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

/**
 *
 *     프로필 추가 버튼을 만들어 놓고,
 *     나의 프로필의 경우, 버튼을 안보이게
 *     상대방의 프로필의 경우, 이미 관심목록에 존재하는지 확인하고
 *     관심목록에 있을경우, 색을 바꿔서 관심목록에서 제거 버튼으로 바꾸기
 *
 */
public class ProfileScreen extends Fragment {

    ProfileScreen(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        ImageButton btnInputMyProfile = (ImageButton) v.findViewById(R.id.btn_my_input_profile);

        btnInputMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InputProfileActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

}
