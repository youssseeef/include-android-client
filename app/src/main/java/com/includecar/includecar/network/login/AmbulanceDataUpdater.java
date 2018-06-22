package com.includecar.includecar.network.login;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by nordicsparrow on 4/13/18.
 */

public class AmbulanceDataUpdater {
    private String URL = "https://car-production-app.herokuapp.com/api/sos/updateAmbulance";
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    public AmbulanceDataUpdater(){
        client = new OkHttpClient();
    }
    public Call updateAmbulanceData(String longitude, String latitude, String ambulanceId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",latitude);
            jsonObject.put("ambulanceId",ambulanceId);
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
   // public Call getAssignments()

}
