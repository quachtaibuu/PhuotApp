package com.example.quachtaibuu.phuotapp;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.adapter.BitmapRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddNewPlaceActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_LOCATION_REQUEST = 10;
    private final int PICK_PLACE_REQUEST = 100;

    private GoogleMap mMapPlace;
    private LocationManager locationManager;
    private LatLng currentLatLng;
    private TextView tvAddNewPlaceLocationPickupName;
    private EditText edAddNewPlaceAddress;
    private EditText edAddNewPlacePhone;
    private EditText edAddNewPlaceDescription;
    private ImageView imgAddNewPlaceChooseImage;
    private List<Uri> lstUriImageChoosen = new ArrayList<>();
    private RecyclerView rcvAddNewPlacePictures;
    private BitmapRecyclerViewAdapter bitmapRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private LocationModel locationModelPickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlace);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.tvAddNewPlaceLocationPickupName = (TextView) findViewById(R.id.tvAddNewPlaceLocationPickupName);

        this.edAddNewPlaceAddress = (EditText) findViewById(R.id.edAddNewPlaceAddress);
        this.edAddNewPlaceDescription = (EditText) findViewById(R.id.edAddNewPlaceDescription);
        this.edAddNewPlacePhone = (EditText) findViewById(R.id.edAddNewPlacePhone);

        this.bitmapRecyclerViewAdapter = new BitmapRecyclerViewAdapter();
        this.bitmapRecyclerViewAdapter.setOnButtonClickListener(new BitmapRecyclerViewAdapter.OnButtonClickListener() {
            @Override
            public void onRemoveClick(int postion) {
                lstUriImageChoosen.remove(postion);
                loadData();
            }
        });
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);

        this.rcvAddNewPlacePictures = (RecyclerView)findViewById(R.id.rcvAddNewPlacePictures);
        this.rcvAddNewPlacePictures.setHasFixedSize(true);
        this.rcvAddNewPlacePictures.setLayoutManager(this.layoutManager);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddNewPlaceAdd:
                Toast.makeText(this, "Thêm thông địa điểm", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            setLocationMaker(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void setLocationMaker(LatLng latLng) {
        this.currentLatLng = latLng;
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        this.mMapPlace.addMarker(marker);
        this.mMapPlace.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            //address.getLocality().replace("City", "")
            this.edAddNewPlaceAddress.setText(address.getAddressLine(0));
            this.tvAddNewPlaceLocationPickupName.setText(address.getAddressLine(1));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            this.locationManager.removeUpdates(this);
        }
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
    public void onMapReady(GoogleMap googleMap) {
        this.mMapPlace = googleMap;
        LatLng center = new LatLng(10.7676563, 106.1338326);
        this.mMapPlace.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 8));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == this.PICK_PLACE_REQUEST) {
                Double lat = data.getDoubleExtra("latitude", 0);
                Double lng = data.getDoubleExtra("longitude", 0);
                this.currentLatLng = new LatLng(lat, lng);
                this.setLocationMaker(this.currentLatLng);
            }else if (requestCode == this.PICK_IMAGE_REQUEST) {
                if (data.getData() != null) {
                    this.addImageUri(data.getData());
                } else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        this.addImageUri(item.getUri());
                    }
                }
                this.loadData();
            }else if(requestCode == this.PICK_LOCATION_REQUEST) {
                this.locationModelPickup = new Gson().fromJson(data.getStringExtra("location"), LocationModel.class);
                this.setLocationPickup();
            }
        }
    }

    public void tvAddNewPlaceChoosenLocation_OnClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, PICK_PLACE_REQUEST);
    }

    public void imgAddNewPlaceChooseImage_OnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void cvAddNewPlaceLocationPickup_OnClick(View v) {
        Intent intent = new Intent(this, LocationPickupActivity.class);
        startActivityForResult(intent, PICK_LOCATION_REQUEST);
    }

    private void loadData() {
        this.bitmapRecyclerViewAdapter.setListData(this.lstUriImageChoosen);
        this.rcvAddNewPlacePictures.setAdapter(this.bitmapRecyclerViewAdapter);

        int itemCount = this.bitmapRecyclerViewAdapter.getItemCount();
        if(itemCount > 0) {
            this.rcvAddNewPlacePictures.smoothScrollToPosition(itemCount - 1);
            this.layoutManager.scrollToPosition(itemCount - 1);
        }
    }

    private void addImageUri(Uri imgUri) {
        if(this.lstUriImageChoosen.indexOf(imgUri) != -1) {
            return;
        }
        this.lstUriImageChoosen.add(imgUri);
    }


    private void setLocationPickup() {
        this.tvAddNewPlaceLocationPickupName.setText(this.locationModelPickup.getName());
    }
}
