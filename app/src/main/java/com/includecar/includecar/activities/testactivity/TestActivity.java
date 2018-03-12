package com.includecar.includecar.activities.testactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.includecar.includecar.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //check if token is valid
        //if yes - stay here.
        //if not
    }
}
