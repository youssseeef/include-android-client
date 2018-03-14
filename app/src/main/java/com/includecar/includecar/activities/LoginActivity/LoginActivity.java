package com.includecar.includecar.activities.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.activities.testactivity.TestActivity;
import com.includecar.includecar.network.login.LoginClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserNameEditText = findViewById(R.id.usernameEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mSignInButton = findViewById(R.id.signinbutton);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked();
            }
        });
    }
    protected void signInClicked(){
        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        LoginClass loginClass = new LoginClass();
        try{
            loginClass.run(username, password, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    backgroundThreadShortToast(LoginActivity.this,"Login Failed, check your internet connection.");

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBody = response.body();
                    String bodyString = responseBody.string();

                    backgroundThreadShortToast(LoginActivity.this,bodyString);
                    Log.e("LOGIN_ACTIVITY",bodyString);
                    if(bodyString.contains("success")){
                        try{
                            JSONObject jsonObject = new JSONObject( bodyString);
                            String token = jsonObject.get("token").toString();
                            Log.e("TOKEN_LOGIN_ACTIVITY",token);
                            Paper.book().write("login_Key",token);
                            moveToTestActivity();
                        }catch(JSONException e){
                            e.printStackTrace();
                            backgroundThreadShortToast(LoginActivity.this,"Login Failed, check your credentials");
                        }

                    }else{
                        backgroundThreadShortToast(LoginActivity.this,"Login Failed, check your credentials");
                    }
                }
            });

        }catch(IOException ex){
            ex.printStackTrace();
        }
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
    //this will be act as a temporary home activity - rough navigation to other activities of the project.
    public void moveToTestActivity(){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
}
