package com.includecar.includecar.activities.mainActivitymedicalprofile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.includecar.includecar.R;
import com.includecar.includecar.fragments.qrscanner.QRScanner;
import com.includecar.includecar.fragments.user.UserFragment;

public class MainActivityMedicalProfile extends AppCompatActivity implements QRScanner.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener {
    private FrameLayout mFrame;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_scan_QR:
                    selectedFragment = QRScanner.newInstance();
                    break;
                case R.id.navigation_medical_editor:
                    selectedFragment = UserFragment.newInstance("","");
                    break;

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMedicalProfile, selectedFragment);
            transaction.commit();
            return  true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_medical_profile);
        mFrame = (FrameLayout) findViewById(R.id.frameLayoutMedicalProfile);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_medical);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        String permission = Manifest.permission.CAMERA;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMedicalProfile, new QRScanner());
            transaction.commit();
        }

    }
    public void onFragmentInteraction(Uri uri){
        return;//implementation
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivityMedicalProfile.this,"permission granted", Toast.LENGTH_SHORT).show();
                // perform your action here
                recreate();

            } else {
                Toast.makeText(MainActivityMedicalProfile.this,"permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
