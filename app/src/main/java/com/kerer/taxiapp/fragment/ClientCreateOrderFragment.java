package com.kerer.taxiapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kerer.taxiapp.R;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class ClientCreateOrderFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "ClientCreateOrderFragment";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private GoogleMap mGoogleMap;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    Geocoder mGeocoder;

    //Marker in user location
    private Marker mUserMarker;
    private Marker mMarkerFrom;
    private Marker mMarkerTo;

    private BitmapDescriptor mUserMarkerIcon;

    @BindView(R.id.order_views_client_route_from)
    EditText mRouteFromEd;
    @BindView(R.id.order_views_client_route_to)
    EditText mRouteToEd;
    @BindView(R.id.order_views_client_order_price_tv)
    TextView mOrderPriceTv;
    @BindView(R.id.order_views_client_order_time_tv)
    TextView mOrderTimeTv;
    @BindView(R.id.order_views_client_order_length_tv)
    TextView mOrderLengthTv;

    public static final ClientCreateOrderFragment newInstance() {
        return new ClientCreateOrderFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();

            mGeocoder = new Geocoder(getActivity());

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_create_order, container, false);

        ButterKnife.bind(this, v);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_client_create_order_map);
        mapFragment.getMapAsync(this);

        initListeners();

        return v;
    }

    private void initListeners(){
        View.OnFocusChangeListener addressFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                   tryDrowMarkerOnMap(v);
                }
            }
        };

        mRouteFromEd.setOnFocusChangeListener(addressFocusChangeListener);
        mRouteToEd.setOnFocusChangeListener(addressFocusChangeListener);
    }

    /**
     * method get addres from edittext and get location with geocoder
     * @param v - view with address
     */
    private void tryDrowMarkerOnMap(View v){
        Log.d(TAG, "try");
        EditText ed = (EditText) v;
        try {
            List<Address> addresses = mGeocoder.getFromLocationName(ed.getText().toString(), 1);
            if (!addresses.isEmpty()){
                //check from what view call this method
                if (v.getId() == R.id.order_views_client_route_from){
                    Address address = addresses.get(0);
                    if (mMarkerFrom != null){
                        mMarkerFrom.remove();
                    }
                    mMarkerFrom = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                }else {
                    Address address = addresses.get(0);
                    if (mMarkerTo != null){
                        mMarkerTo.remove();
                    }
                    mMarkerTo = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
                }
            }else {
                ed.setError(getString(R.string.cannot_find_address));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    //GOOGLE API METHODS

    /**
     * @return is play services aviable
     */
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

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO GET PERMISSION
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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

    }

    @Override
    public void onLocationChanged(Location location) {
        //adding user location marker on map
       mUserMarker = mGoogleMap.addMarker(new MarkerOptions()
                       /*.icon(mUserMarkerIcon)*/
                       .position(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    //FRAGMENT LIFE CYCLE
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()){
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }
}
