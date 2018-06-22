package com.includecar.includecar.network.login;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class QRNetworking {
    private String URL = "https://car-production-app.herokuapp.com/api2/carAddQR";
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public QRNetworking(){
        client = new OkHttpClient();


    }
    public Call sendProfileViaQR(String qrCode, String username, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("carId", qrCode);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String tokenKey = Paper.book().read("login_Key");
        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL)
                .header("Authorization",tokenKey)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
