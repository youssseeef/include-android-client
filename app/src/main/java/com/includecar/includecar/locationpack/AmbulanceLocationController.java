package com.includecar.includecar.locationpack;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by nordicsparrow on 4/13/18.
 */

public class AmbulanceLocationController {
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 3123;
    private static boolean permissionGiven = true ;


    private LocationManager locationManager;
    private Context mContext;
    public AmbulanceLocationController(Context mContext){
        this.mContext = mContext;
    }
    public LocationManager getLocationManager(FragmentActivity activity){

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions((AppCompatActivity) mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }else{
            locationManager = (LocationManager)
                    activity.getSystemService(Context.LOCATION_SERVICE);
            return locationManager;
        }
        return null;
    }

    public boolean isPermissionGranted(){
        return permissionGiven;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

}