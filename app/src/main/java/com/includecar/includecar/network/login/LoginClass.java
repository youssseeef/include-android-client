package com.includecar.includecar.network.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by nordicsparrow on 3/2/18.
 */

public class LoginClass {
    private String URL = "https://car-production-app.herokuapp.com/api2/signin";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    public LoginClass(){
        client = new OkHttpClient();
    }
    public Call run(String username, String password, Callback callback) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
