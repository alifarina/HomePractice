package com.poject.dalithub.screens;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;

public class PickLocationActivity extends FragmentActivity implements OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private String TAG = "InviteMEmber";
    private UserPreferences mPref;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        mPref = new UserPreferences(this);
        initViews();
        getMap();
        setScreenListeners();
    }

    private void getMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("PICK LOCATION");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;

            default:
                break;
        }

    }


    private void goBackScreen() {
        this.finish();
    }

    private void drawMarker(LatLng currentPosition) {
        googleMap.clear();

//  convert the location object to a LatLng object that can be used by the map API
        // LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

// zoom to the current location
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

// add a marker to the map indicating our current position
        googleMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + currentPosition.latitude + "Lng:" + currentPosition.longitude));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                AppUtils.showToast(PickLocationActivity.this, latLng.latitude + " " + latLng.longitude);
                LatLng loc = new LatLng(latLng.latitude, latLng.longitude);
                drawMarker(loc);
//                googleMap.addMarker(new MarkerOptions().position(myLoc).title("My Location"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));

            }
        });
        googleMap.setMyLocationEnabled(true);
        setUpMapIfNeeded();
        // drawMarker(googleMap.getMyLocation());
    }

    private void setUpMapIfNeeded() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//        drawMarker(loc);
        setUpMapIfNeeded();
    }
}
