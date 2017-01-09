package com.kerer.taxiapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.interfaces.FirebaseDatabaseReferences;
import com.kerer.taxiapp.interfaces.GoogleDirectionsResultStatus;
import com.kerer.taxiapp.interfaces.OrderStatuses;
import com.kerer.taxiapp.model.Order;
import com.kerer.taxiapp.model.RouteResponse;
import com.kerer.taxiapp.rest.GoogleDirectionsApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */

public class ClientCreateOrderFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleDirectionsResultStatus, FirebaseDatabaseReferences, OrderStatuses {

    private static final String TAG = "ClientCreateOrderFragment";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private GoogleMap mGoogleMap;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    //user location marker
    private Marker mUserMarker;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @BindView(R.id.order_views_client_order_status_ed)
    TextView mOrderStatusTv;
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
    @BindView(R.id.order_views_client_create_order_fab)
    FloatingActionButton mCreateOrderFab;

    public static final ClientCreateOrderFragment newInstance() {
        return new ClientCreateOrderFragment();
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

            //checking if not finished order is in user

            mDatabase
                    .child(CLIENTS)
                    .child(mUser.getUid())
                    .child(ACTIVE_ORDERS)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                Order order = dataSnapshot1.getValue(Order.class);

                                switch (order.getmStatus()){
                                    case STATUS_CREATED:
                                        break;
                                    case STATUS_GETED:
                                        break;
                                    case STATUS_BY_ORIGIN:
                                        break;
                                    case STATUS_BY_DESTINATION:
                                        break;
                                    case STATUS_PAYED:
                                        mDatabase.child(CLIENTS)
                                                .child(mUser.getUid())
                                                .child(ACTIVE_ORDERS)
                                                .child(dataSnapshot1.getKey())
                                                .removeValue();

                                        mDatabase.child(CLIENTS)
                                                .child(mUser.getUid())
                                                .child(ORDERS)
                                                .push()
                                                .setValue(order);
                                        break;
                                    case STATUS_CANCELED:
                                        Toast.makeText(getActivity(), "YOURS ORDER VAS CANCELED", Toast.LENGTH_SHORT)
                                                .show();
                                        mDatabase.child(CLIENTS)
                                                .child(mUser.getUid())
                                                .child(ACTIVE_ORDERS)
                                                .child(dataSnapshot1.getKey())
                                                .removeValue();

                                        mDatabase.child(CLIENTS)
                                                .child(mUser.getUid())
                                                .child(ORDERS)
                                                .push()
                                                .setValue(order);
                                        break;
                                }
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
        View v = inflater.inflate(R.layout.fragment_client_create_order, container, false);

        ButterKnife.bind(this, v);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_client_create_order_map);
        mapFragment.getMapAsync(this);

        initListeners();

        return v;
    }

    private void initListeners() {
        //Route EditText`s losteners

        //fab listener
        mCreateOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origin = mRouteFromEd.getText().toString();
                String destination = mRouteToEd.getText().toString();
                if (origin.length() > 0 && destination.length() > 0) {
                    GoogleDirectionsApiService service = GoogleDirectionsApiService.retrofit.create(GoogleDirectionsApiService.class);
                    Call<RouteResponse> getDirectionInfo = service.getDirection(origin, destination, getString(R.string.google_maps_key));
                    getDirectionInfo.enqueue(new Callback<RouteResponse>() {
                        @Override
                        public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                            Log.d(TAG, new Gson().toJson(response.body()));
                            if (response.body().getStatus().equals(OK)) {
                                drawPolyline(response.body());
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.something_goes_wrong), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RouteResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), getString(R.string.no_interner), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.fields_must_filled), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    /**
     * drow polyline on map
     *
     * @param response model from google directions API, curently have just polyline options
     */
    private void drawPolyline(RouteResponse response) {
        mGoogleMap.clear();

        PolylineOptions line = new PolylineOptions();
        List<LatLng> mPoints = PolyUtil.decode(response.getPoints());
        line.width(10f).color(R.color.colorOrderStatusBackground);
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < mPoints.size(); i++) {
            if (i == 0) {
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(mPoints.get(i));
                mGoogleMap.addMarker(startMarkerOptions);
            } else if (i == mPoints.size() - 1) {
                MarkerOptions endMarkerOptions = new MarkerOptions()
                        .position(mPoints.get(i));
                mGoogleMap.addMarker(endMarkerOptions);
            }
            line.add(mPoints.get(i));
            latLngBuilder.include(mPoints.get(i));
        }


        mGoogleMap.addPolyline(line);

        createOrder();
    }

    /**
     * method insert to DB new order
     */
    private void createOrder() {
        String key = mDatabase.child(ORDERS).push().getKey();
        Order order = bindOrder();
        Map<String, Object> orderValues = order.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + ORDERS + "/" + key, orderValues);
        childUpdates.put("/" + CLIENTS + "/" + mUser.getUid() + "/" + ACTIVE_ORDERS + "/" + key, orderValues);

        //set order to client and to active orders
        mDatabase.updateChildren(childUpdates);
    }

    /**
     * @return order model for Firebase
     */
    private Order bindOrder() {
        Order order = new Order();
        order.setmClientUid(mUser.getUid());
        order.setmOrigin(mRouteFromEd.getText().toString());
        order.setmDestination(mRouteToEd.getText().toString());
        order.setmStatus(STATUS_CREATED);

        return order;
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
     */
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
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
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
