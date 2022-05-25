/*
05.25 박우천
Data는 RecyclerAdaper 데이터를 보내는 역할입니다.
현재의 구성요소: 이름, 자기소개, 프로필 사진
추가할 구성요소: 성격유형
*/

package com.example.roomate;

public class Data {

    private String name; //이름
    private String introduce; //자기소개
    private int resId; //프로필 사진

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getContent() {
        return introduce;
    }

    public void setContent(String introduce) {
        this.introduce = introduce;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}