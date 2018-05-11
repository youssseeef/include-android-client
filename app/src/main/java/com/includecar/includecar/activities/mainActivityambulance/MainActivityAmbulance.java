package com.includecar.includecar.activities.mainActivityambulance;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.includecar.includecar.R;
import com.includecar.includecar.fragments.ambulancestatus.AmbulanceStatusFragment;
import com.includecar.includecar.fragments.home.HomeFragment;
import com.includecar.includecar.fragments.notificationmaps.NotificationsMapsFragment;
import com.includecar.includecar.fragments.user.UserFragment;

public class MainActivityAmbulance extends AppCompatActivity implements UserFragment.OnFragmentInteractionListener,
        NotificationsMapsFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        AmbulanceStatusFragment.OnFragmentInteractionListener{

    private FrameLayout mFrame;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = AmbulanceStatusFragment.newInstance("12","123");
                    break;
                case R.id.navigation_map:
                    selectedFragment = NotificationsMapsFragment.newInstance("as","123");
                    break;
                case R.id.navigation_user:
                    selectedFragment = UserFragment.newInstance("as","123");
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrame = (FrameLayout) findViewById(R.id.frameLayout);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, new AmbulanceStatusFragment());
        transaction.commit();
    }
    public void onFragmentInteraction(Uri data){

    }

}
