package com.includecar.includecar.activities.rescueActivity;

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
import com.includecar.includecar.fragments.rescuemap.RescueMapFragment;
import com.includecar.includecar.fragments.rescuestatus.RescueStatusFragment;
import com.includecar.includecar.fragments.user.UserFragment;
import com.includecar.includecar.fragments.userdisplaydetails.DisplayUserDetailsFragment;
import com.includecar.includecar.helperclasses.CustomViewPager;
import com.includecar.includecar.helperclasses.ViewPagerAdapter;

public class RescueCarActivity extends AppCompatActivity implements
        RescueMapFragment.OnFragmentInteractionListener,
 RescueStatusFragment.OnFragmentInteractionListener{

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
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_car);
        mPager = findViewById(R.id.viewpager2);
        ViewPagerAdapter adapter = new ViewPagerAdapter (RescueCarActivity.this.getSupportFragmentManager());
        adapter.addFragment(RescueStatusFragment.newInstance("awe","aweawe"), "Rescue Status");
        adapter.addFragment(RescueMapFragment.newInstance("awecaw","wervwer"), "Map");
        mPager.setAdapter(adapter);
        BottomNavigationView navigation = findViewById(R.id.navigation_rescue);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void onFragmentInteraction(Uri data){

    }

}
