package com.includecar.includecar.fragments.notificationmaps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.includecar.includecar.R;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsMapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsMapsFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Ambulance_Location = "param1";
    private static final String Car_Location = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mNavigateButton;
    private OnFragmentInteractionListener mListener;

    public NotificationsMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsMapsFragment newInstance(String param1, String param2) {
        NotificationsMapsFragment fragment = new NotificationsMapsFragment();
        Bundle args = new Bundle();
        args.putString(Ambulance_Location, param1);
        args.putString(Car_Location, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Ambulance_Location);
            mParam2 = getArguments().getString(Car_Location);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_maps, container, false);
        mNavigateButton = view.findViewById(R.id.navigation_mapping_app_button);
        mNavigateButton.setVisibility(View.GONE);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_notification_fragment_map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String latitude = Paper.book().read("ambulance_my_position_latitude");
        String longitude = Paper.book().read("ambulance_my_position_longitude");
        if( latitude != null && latitude.trim().equals("") && longitude != null && longitude.trim().equals("")){
            googleMap.addMarker(new MarkerOptions().position(new LatLng(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude)
            )));
            googleMap.getMaxZoomLevel();
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude)
            )));
        }

        updateMap(googleMap);
    }
    public void updateMap(final GoogleMap googleMap){
        if(googleMap != null){
            //use this to show both points accident and user
            //create a button to open navigation
            //create a point to delete accident assignment from server to accept new ones
            googleMap.clear();
            String latitude = Paper.book().read("ambulance_my_position_latitude");
            String longitude = Paper.book().read("ambulance_my_position_longitude");
            final String car_lat = Paper.book().read("car_latitude");
            final String car_lng = Paper.book().read("car_longitude");
            Log.d("MAP22",String.valueOf(latitude != null ));
            Log.d("MAP2233",String.valueOf(longitude != null ));

            if( latitude != null && !latitude.trim().equals("") && longitude != null && !longitude.trim().equals("")){
                if(car_lat == null || car_lat.trim().equals("") || car_lng == null || car_lng.trim().equals("")){
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)
                    )).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance_bitmap)));
                    //accident marker
                    googleMap.setMinZoomPreference(googleMap.getMaxZoomLevel()-2);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)
                    )));
                }else{
                    //adds markers

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(
                            Double.parseDouble(latitude),
                            Double.parseDouble(longitude)
                    )).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance_bitmap)));

                    googleMap.addMarker(new MarkerOptions().position(new LatLng(
                            Double.parseDouble(car_lat),
                            Double.parseDouble(car_lng)
                    )).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
                    mNavigateButton.setOnClickListener(null);
                    mNavigateButton.setVisibility(View.VISIBLE);
                    mNavigateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q="+car_lat+","+car_lng));
                            startActivity(intent);
                        }
                    });
                    //sets bounds
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    LatLng ambPos = new LatLng(Double.parseDouble(latitude),
                            Double.parseDouble(longitude));
                    LatLng car = new LatLng(Double.parseDouble(car_lat),
                            Double.parseDouble(car_lng));
                    builder.include(ambPos);
                    builder.include(car);
                    LatLngBounds bounds = builder.build();
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    if(getActivity() != null){
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    }
                    int height = displaymetrics.heightPixels;
                    int width = displaymetrics.widthPixels;
                    int padding = (int) (width * 0.15); // offset from edges of the map 10% of screen
                    if ((height != 0) && width != 0){
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        googleMap.animateCamera(cu);
                    }

                }

            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MAP","UPDAGI");
                updateMap(googleMap);
            }
        }, 1000);
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
}
