package com.includecar.includecar.activities.startactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.activities.LoginActivity.LoginActivity;
import com.includecar.includecar.activities.mainActivityambulance.MainActivityAmbulance;
import com.includecar.includecar.activities.mainActivitymedicalprofile.MainActivityMedicalProfile;
import com.includecar.includecar.network.login.TokenValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StartActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Paper is like an ORM for SQLLite..
                Paper.init(StartActivity.this);
                //I try to get a pre-saved login token.
                String savedToken = Paper.book().read("login_Key","null");
                //token validator
                TokenValidator tokenValidator = new TokenValidator();
                try{
                    tokenValidator.run(savedToken, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //network/other type of error
                            // redirect to home screen
                            moveToLoginActivity();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //got a response from the server
                            ResponseBody responseBody = response.body();

                            String responseString = responseBody.string().toString();
                            // Use status codes obviously.
                            if(responseString.contains("unauthorized")){
                                moveToLoginActivity();
                                //invalid token//can't login
                            }else if(responseString.contains("success")){
                                try{
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    String userType = jsonObject.getString("userType");
                                    String userId = jsonObject.getString("userType");
                                    if(userType.equals("car")){
                                        moveToLoginActivity();//cars don't have a mobile app.
                                    }else if(userType.equals("ambulance")){
                                        moveToMainActivity();
                                    }else if(userType.equals("medicalProfile")){
                                        moveToMainMedicalActivity();
                                    }else{
                                        moveToLoginActivity();
                                    }
                                }catch(JSONException je){

                                }
                            }
                        }
                    });
                }catch(IOException ex){

                }

                //check if token is valid
                //if yes - stay here.//logged in
                //if not - get out//logged out- clear storage from token - go to home screen

            }
        },SPLASH_DISPLAY_LENGTH);
        //init paper
    }
    public void moveToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivityAmbulance.class);
        startActivity(intent);
        finish();
    }
    public void moveToMainMedicalActivity(){
        Intent intent = new Intent(this, MainActivityMedicalProfile.class);
        startActivity(intent);
        finish();
    }
    public static void backgroundThreadShortToast(final Context context,
                                                  final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
