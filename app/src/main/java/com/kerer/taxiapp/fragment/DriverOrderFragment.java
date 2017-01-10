package com.kerer.taxiapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.interfaces.FirebaseDatabaseReferences;
import com.kerer.taxiapp.interfaces.OrderStatuses;
import com.kerer.taxiapp.model.Order;

/**
 * Created by ivan on 10.01.17.
 */

public class DriverOrderFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,FirebaseDatabaseReferences, OrderStatuses{
    private static final String TAG = "DriverOrderFragment";

    public static final String ARG_ORDER_KEY = "orderKey";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    private Marker mUserMarker;

    private Order mOrder;

    public static DriverOrderFragment newInstance(String orderKey) {
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_KEY, orderKey);
        DriverOrderFragment fragment = new DriverOrderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();

            //init Firebase
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            if (mUser == null) {
                getActivity().finish();
            }
            mDatabase = FirebaseDatabase.getInstance().getReference();

            String orderKey = getArguments().getString(ARG_ORDER_KEY);

            mDatabase
                    .child(DRIVERS)
                    .child(mUser.getUid())
                    .child(ACTIVE_ORDERS)
                    .child(orderKey)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                mOrder = dataSnapshot1.getValue(Order.class);
                                onOrderStatusChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_order, container, false);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       mGoogleMap = googleMap;
    }

    /**
     * calls just when order info in firebase database was changed.
     * Show actual info in layout
     */
    private void onOrderStatusChanged(){
        if (mOrder != null){
            Log.d(TAG, String.valueOf(mOrder.getmStatus()));
            switch (mOrder.getmStatus()){
                case STATUS_GETED:
                    break;
                case STATUS_BY_ORIGIN:
                    break;
                case STATUS_BY_DESTINATION:
                    break;
                case STATUS_PAYED:
                    break;
                case STATUS_CANCELED:
                    break;
            }
        }
    }

    /**
     * Initializing google api client for getting user location
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        R.string.device_not_supported, Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    //GOOGLE API CALLBACKS
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), R.string.something_goes_wrong, Toast.LENGTH_SHORT)
                .show();
        getActivity().finish();
    }

    //LOCATION UPDATES
    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO GET PERMISSION
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mUserMarker = mGoogleMap.addMarker(new MarkerOptions()
                .title(getString(R.string.your_location))
                .position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    //FRAGMENT LIFE CYCLE
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
