package com.includecar.includecar.activities.testactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.activities.LoginActivity.LoginActivity;
import com.includecar.includecar.activities.mainActivity.MainActivity;
import com.includecar.includecar.network.login.TokenValidator;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TestActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Paper.init(TestActivity.this);
                //get token
                String savedToken = Paper.book().read("login_Key","null");
                TokenValidator tokenValidator = new TokenValidator();
                try{
                    tokenValidator.run(savedToken, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //network error//redirect to home screen
                            moveToLoginActivity();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseBody responseBody = response.body();
                            String responseString = responseBody.string().toString();
                            backgroundThreadShortToast(TestActivity.this,responseString);
                            //this is dumb but works for now. TODO: Use status codes obviously.
                            if(responseString.contains("Unauthorized")){
                                moveToLoginActivity();//invalid token
                            }else if(responseString.contains("OK")){
                                moveToMainActivity();
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
    }
    public void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
