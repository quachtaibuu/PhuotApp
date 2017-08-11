package com.example.quachtaibuu.phuotapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private View mView;

    private LatLng latLngPickup;
    private Marker mMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng center = new LatLng(10.7676563, 106.1338326);
        //mMap.addMarker(new MarkerOptions().position(center).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 8));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 10, this);

        Intent intent = getIntent();
        if(intent != null) {
            latLngPickup = new Gson().fromJson(intent.getStringExtra("currentLatLng"), LatLng.class);
            setLocationMaker(latLngPickup);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latLngPickup = latLng;
                setLocationMaker(latLng);
            }
        });

    }

    private void setLocationMaker(LatLng latLng) {
        if(mMaker != null) {
            mMaker.remove();
        }
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            markerOptions.title(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMaker = mMap.addMarker(markerOptions);
    }

    @Override
    public void onLocationChanged(Location location) {
//        if(location != null) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions marker = new MarkerOptions();
//            marker.position(latLng);
//            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            try {
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                marker.title(addresses.get(0).getAddressLine(0));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            this.mMap.addMarker(marker);
//        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Gson gson = new Gson();
        Intent intent = new Intent(MapsActivity.this, AddNewPlaceActivity.class);
        intent.putExtra("latLngPickup", gson.toJson(latLngPickup));
        setResult(RESULT_OK, intent);
        finish();
    }

}
