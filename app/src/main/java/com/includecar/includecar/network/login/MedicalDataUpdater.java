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

public class MedicalDataUpdater {
    private String URL = "https://car-production-app.herokuapp.com/api2/postUserData";
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public MedicalDataUpdater(){
        client = new OkHttpClient();


    }
    public Call updateUserData(
            String fullName,String email, String phoneNumber, String emergencyContactName,
            String emergencyPhoneNumber, String birthYear, String birthMonth, String birthDay,
            String surgicalHistory, String currentMedications, String allergiesToDrugs, String bloodGroup,
            String addict, String smoker, String cancer, String chronicObstructive,
            String disease, String clotticDisorder, String heartFailure, String diabetes,
            String emhysema, String hepatitis, String hyperTension, String myocardialInfraction,
            String seizures, String strokes,
            Callback callback

    ){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("fullName",fullName);
            jsonObject.put("username",email);
            jsonObject.put("phoneNumber",phoneNumber);
            jsonObject.put("emergencyContactName",emergencyContactName);
            jsonObject.put("emergencyPhoneNumber",emergencyPhoneNumber);
            jsonObject.put("birthYear",birthYear);
            jsonObject.put("birthMonth",birthMonth);
            jsonObject.put("birthDay",birthDay);
            jsonObject.put("surgicalHistory",surgicalHistory);
            jsonObject.put("currentMedications",currentMedications);
            jsonObject.put("allergiesToDrugs",allergiesToDrugs);
            jsonObject.put("bloodGroup",bloodGroup);
            jsonObject.put("addict",addict);
            jsonObject.put("smoker",smoker);
            jsonObject.put("cancer",cancer);
            jsonObject.put("chronicObstructive",chronicObstructive);
            jsonObject.put("disease",disease);
            jsonObject.put("clotticDisorder",clotticDisorder);
            jsonObject.put("heartFailure",heartFailure);
            jsonObject.put("diabetes",diabetes);
            jsonObject.put("emhysema",emhysema);
            jsonObject.put("hepatitis",hepatitis);
            jsonObject.put("hyperTension",hyperTension);
            jsonObject.put("myocardialInfraction",myocardialInfraction);
            jsonObject.put("seizures",seizures);
            jsonObject.put("strokes",strokes);
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
