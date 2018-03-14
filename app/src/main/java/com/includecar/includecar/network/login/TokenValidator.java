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

/**
 * Created by nordicsparrow on 3/12/18.
 * this class will mainly validate the token.
 * In case of a valid token -> return a valid token JSON
 * In case of an Invalid token -> return a failure JSON
 * This will be used to redirect to the token activity on failure.
 */

public class TokenValidator {
    private String URL = "https://car-production-app.herokuapp.com/api2/validateToken";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    public TokenValidator(){
        client = new OkHttpClient();
    }
    public Call run(String token, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON,"{}");
        Request request = new Request.Builder()
                .url(URL)
                .header("Authorization",token)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
