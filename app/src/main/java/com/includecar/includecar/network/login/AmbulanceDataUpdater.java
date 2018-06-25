package com.includecar.includecar.network.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

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
    //will be used to update the ambulance data and get the associatedCar if any
    private String URL_UPDATE_AMBULANCE = "https://car-production-app.herokuapp.com/api/sos/updateAmbulance";
    //will be used to get the ambulance data at the beginning. mainly to set the status of the button
    private String URL_GET_AMBULANNCE_DATA = "https://car-production-app.herokuapp.com/api/sos/getAmbulanceData";
    //Used to manually delete the associated car and accident
    private String URL_DELETE = "https://car-production-app.herokuapp.com/api/sos/endAccident";

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;
    public AmbulanceDataUpdater(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(4, TimeUnit.SECONDS);
//        builder.readTimeout(3, TimeUnit.SECONDS);
//        builder.writeTimeout(30, TimeUnit.SECONDS);
        client = builder.build();
    }
    public Call updateAmbulanceData(String longitude, String latitude, String ambulanceId, String buttonStatus, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("longitude",longitude);
            jsonObject.put("latitude",latitude);
            jsonObject.put("ambulanceId",ambulanceId);
            jsonObject.put("ambulanceReadyToTake",buttonStatus);

        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL_UPDATE_AMBULANCE)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    //this call will be used
    public Call getAmbulanceData(String ambulanceId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("ambulanceId",ambulanceId);

        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL_GET_AMBULANNCE_DATA)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public Call deleteAmbulanceData(String ambulanceId, Callback callback){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("ambulanceId",ambulanceId);

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
