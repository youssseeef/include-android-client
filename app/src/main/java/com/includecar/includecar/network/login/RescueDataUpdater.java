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

public class RescueDataUpdater {
    //will be used to update the ambulance data and get the associatedCar if any
    private String URL_UPDATE_RESCUE = "https://car-production-app.herokuapp.com/api/sos/updateRescue";
    //will be used to get the ambulance data at the beginning. mainly to set the status of the button
    private String URL_GET_RESCUE_DATA = "https://car-production-app.herokuapp.com/api/sos/getRescueData";
    //Used to manually delete the associated car and accident
    private String URL_DELETE = "https://car-production-app.herokuapp.com/api/sos/endRescueAccident";

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    public RescueDataUpdater(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(4, TimeUnit.SECONDS);
//        builder.readTimeout(3, TimeUnit.SECONDS);
//        builder.writeTimeout(30, TimeUnit.SECONDS);
        client = builder.build();
    }
    public Call updateRescueData(String longitude, String latitude, String rescueId, String buttonStatus, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",latitude);
            jsonObject.put("rescueId",rescueId);
            jsonObject.put("rescueReadyToTake",buttonStatus);

        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL_UPDATE_RESCUE)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    //this call will be used
    public Call getRescueData(String rescueId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("rescueId",rescueId);

        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL_GET_RESCUE_DATA)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public Call deleteRescueData(String rescueId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("rescueId",rescueId);

        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL_DELETE)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
