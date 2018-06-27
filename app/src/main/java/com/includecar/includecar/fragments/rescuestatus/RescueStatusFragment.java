package com.includecar.includecar.fragments.rescuestatus;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.includecar.includecar.R;
import com.includecar.includecar.activities.LoginActivity.LoginActivity;
import com.includecar.includecar.locationpack.AmbulanceLocationController;
import com.includecar.includecar.network.login.AmbulanceDataUpdater;
import com.includecar.includecar.network.login.RescueDataUpdater;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RescueStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RescueStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RescueStatusFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LocationListener{


    private Switch toggleSwitchRescueRequests;

    private TextView mStatusTextView;
    private TextView mStatusTextView2;
    private Button mEndAccidentButton;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private double longitude = 0.0;
    private double latitude = 0.0;

    private AmbulanceLocationController amlc;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RescueStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AmbulanceStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RescueStatusFragment newInstance(String param1, String param2) {

        RescueStatusFragment fragment = new RescueStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){

        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForButtonStatus();
            }
        },2000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rescue_status, container, false);
        toggleSwitchRescueRequests = (Switch) v.findViewById(R.id.toggle_ambulance_search_switch);
        mStatusTextView = (TextView) v.findViewById(R.id.request_status_ambulance);
        mStatusTextView2= (TextView) v.findViewById(R.id.request_status_ambulance2);
        mEndAccidentButton = v.findViewById(R.id.END_ACCIDENT_BUTTON);
        mEndAccidentButton.setVisibility(View.GONE);
        mEndAccidentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RescueDataUpdater adu = new RescueDataUpdater();
                String rescueId = Paper.book().read("login_username");

                adu.deleteRescueData(rescueId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code() == 200){
                            //success
                            if(RescueStatusFragment.this.getActivity() != null){
                                Paper.book().delete("car_latitude");
                                Paper.book().delete("car_longitude");
                                Paper.book().delete("car_Id");
                                RescueStatusFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toggleSwitchRescueRequests.setChecked(false);
                                        mEndAccidentButton.setVisibility(View.GONE);

                                    }
                                });
                            }

                        }else {
                            //failed
                        }
                        response.close();
                    }

                });
            }
        });
        toggleSwitchRescueRequests.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                checkForAssignedAccidents();
                if(isChecked){
                    mStatusTextView.setText(R.string.waitingStatusAmbulance);
                    mStatusTextView2.setText(R.string.SearchingForAccidents);
                    amlc = new AmbulanceLocationController(getActivity());
                    if(amlc.isPermissionGranted()){
                        LocationManager locMan = amlc.getLocationManager(RescueStatusFragment.this.getActivity());
                        if(locMan != null){
                            try{
                                locMan.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER, 1000, 1, RescueStatusFragment.this );
                            }catch (SecurityException ex){

                            }
                        }else{
                            toggleSwitchRescueRequests.setChecked(false);
                            if(locMan != null){
                                locMan.removeUpdates(RescueStatusFragment.this);
                            }
                        }

                    }else{
                        //amlc permission not granted.
                    }
                    //should run update routing here here TODO

                }else{
                    mStatusTextView.setText(R.string.notWaitingStatusAmbulance);
                    mStatusTextView2.setText(R.string.not_searching_for_accidents);
                    //stop checking and set server data to not waiting for requests.
                }
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AmbulanceLocationController.MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(amlc.getLocationManager() != null){
                        amlc.setLocationManager((LocationManager)
                                getActivity().getSystemService(Context.LOCATION_SERVICE));
                    }
                } else {
                    toggleSwitchRescueRequests.setChecked(false);
                    toggleSwitchRescueRequests.setEnabled(false);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        if(latitude != 0 && longitude != 0){
            Paper.book().write("rescue_my_position_latitude", String.valueOf(latitude));
            Paper.book().write("rescue_my_position_longitude", String.valueOf(longitude));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void checkForAssignedAccidents(){
        RescueDataUpdater adu = new RescueDataUpdater();
        String rescueId = Paper.book().read("login_username");
        if(latitude != 0 && longitude != 0){
            adu.updateRescueData(String.valueOf(longitude), String.valueOf(latitude),rescueId,String.valueOf(toggleSwitchRescueRequests.isChecked()), new Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("FAILED" ,e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.code() == 200){

                        ResponseBody responseBody = response.body();
                        String bodyString = responseBody.string();
                        Log.e("TATEARAWERAWE",bodyString);
                        try{
                            JSONObject jsonObject = new JSONObject(bodyString);
                            if(jsonObject.has("carAssigned")){
                                String carAssigned = jsonObject.get("carAssigned").toString();
                                String carlocation = jsonObject.get("carLocation").toString();
                                JSONObject jsonObject2 = new JSONObject(carAssigned);
                                JSONObject jsonObject3 = new JSONObject(carlocation);
                                //fetching data from the reesponse
                                final String latitude = jsonObject3.get("latitude").toString();
                                final String longitude = jsonObject3.get("longitude").toString();
                                final String carId = jsonObject2.get("carId").toString();

                                //Storing them for other activities to access.
                                Paper.book().write("car_latitude", latitude);
                                Paper.book().write("car_longitude", longitude);
                                Paper.book().write("car_Id", carId);


                                if(RescueStatusFragment.this.getActivity() != null){
                                    RescueStatusFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Handle UI here
                                            mEndAccidentButton.setVisibility(View.VISIBLE);
                                            mStatusTextView2.setText("Accident Happened! Check the map!");}
                                    });
                                }
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }else{
                        //error

                    }
                    response.close();
                }

            });
        }

        if(toggleSwitchRescueRequests.isChecked()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkForAssignedAccidents();

                }
            },2000);
        }

    }
    //that runs at the beginning
    public void checkForButtonStatus(){
        RescueDataUpdater adu = new RescueDataUpdater();
        String rescueId = Paper.book().read("login_username");
        adu.getRescueData(rescueId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    //not error
                    ResponseBody responseBody = response.body();
                    String bodyString = responseBody.string();
                    Log.e("GETGET",bodyString);

                    try{
                        JSONObject jsonObject = new JSONObject(bodyString);
                        final String ambulanceReadyToTake = jsonObject.get("rescueReadyToTake").toString();
                        Log.d("RESCUE_READ", ambulanceReadyToTake);


                        if(RescueStatusFragment.this.getActivity() != null){
                            RescueStatusFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Handle UI here
                                    if(ambulanceReadyToTake.equals("true")){
                                        mStatusTextView.setText(R.string.waitingStatusAmbulance);
                                        toggleSwitchRescueRequests.setChecked(true);
                                    }else{
                                        mStatusTextView.setText(R.string.notWaitingStatusAmbulance);
                                        toggleSwitchRescueRequests.setChecked(false);
                                    }

                                }
                            });
                        }

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                response.close();
            }
        });

    }
}
