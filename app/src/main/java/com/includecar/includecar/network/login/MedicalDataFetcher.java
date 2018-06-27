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

public class MedicalDataFetcher {
    private String URL = "https://car-production-app.herokuapp.com/api2/getUserData";
    private String URL2 = "https://car-production-app.herokuapp.com/api2/getAmbulanceUsers";
    private String URL3 = "https://car-production-app.herokuapp.com/api2/getAmbulanceUser";
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public MedicalDataFetcher(){ client = new OkHttpClient();

    }
    public Call fetchUserData(String userEmail, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userEmail);
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
    public Call fetchUserDataReloaded(String userEmail, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userEmail);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String tokenKey = Paper.book().read("login_Key");
        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL3)
                .header("Authorization",tokenKey)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;

    }
    public Call fetchAllUsers(String carId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("carId", carId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String tokenKey = Paper.book().read("login_Key");
        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL2)
                .header("Authorization",tokenKey)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;

    }

}
