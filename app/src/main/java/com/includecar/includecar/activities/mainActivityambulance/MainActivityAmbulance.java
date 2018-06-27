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
import com.includecar.includecar.fragments.userdisplaydetails.DisplayUserDetailsFragment;
import com.includecar.includecar.helperclasses.CustomViewPager;
import com.includecar.includecar.helperclasses.ViewPagerAdapter;

public class MainActivityAmbulance extends AppCompatActivity implements
        NotificationsMapsFragment.OnFragmentInteractionListener,
        AmbulanceStatusFragment.OnFragmentInteractionListener,
        DisplayUserDetailsFragment.OnFragmentInteractionListener{

    private CustomViewPager mPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mPager.setCurrentItem(0);
                    break;
                case R.id.navigation_map:
                    mPager.setCurrentItem(1);
                    break;
                case R.id.navigation_user:
                    mPager.setCurrentItem(2);
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = findViewById(R.id.viewpager1);
        ViewPagerAdapter adapter = new ViewPagerAdapter (MainActivityAmbulance.this.getSupportFragmentManager());
        adapter.addFragment(AmbulanceStatusFragment.newInstance("awe","aweawe"), "Ambulance Status");
        adapter.addFragment(NotificationsMapsFragment.newInstance("awecaw","wervwer"), "Map");
        adapter.addFragment(DisplayUserDetailsFragment.newInstance("",""), "Medical Data");
        mPager.setAdapter(adapter);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void onFragmentInteraction(Uri data){

    }

}
