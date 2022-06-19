package com.example.roomate;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static String toJSon(JsonString jsonString) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("KakaoID", jsonString.getKakaoid());
            jsonObj.put("Nickname", jsonString.getNickname());

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
