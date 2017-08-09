package com.example.quachtaibuu.phuotapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.quachtaibuu.phuotapp.adapter.PlaceImageAdapter;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final String DIRECT_API_KEY = "AIzaSyDjXHNyOW7EZU42ubxtQ8RgYQZTyzf6_EA";
    public static final String EXTRA_PLACE_KEY = "place_key";

    private int currentPage = 0;
    private ViewPager vpPlaceDetailImages;
    private PlaceImageAdapter placeImageAdapter;
    private CircleIndicator ciPlaceDetailImages;
    private TextView tvPlaceDetailTitle;
    private TextView tvPlaceDetailAddress;
    private TextView tvPlaceDetailDescription;

    private PlaceModel placeModel;
    private List<String> lstData = new ArrayList<>();
    private GoogleMap mMap;

    private LatLng latLngPlace;
    private LatLng latLngCurrent;

    private LocationManager locationManager;
    private String mPlaceKey;
    private DatabaseReference mPlaceRefernce;
    private ValueEventListener mPlaceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        this.mPlaceKey = getIntent().getStringExtra(this.EXTRA_PLACE_KEY);
        if(this.mPlaceKey == null) {
            throw new IllegalArgumentException("Key not found!");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlaceDetail);
        mapFragment.getMapAsync(this);

        this.mPlaceRefernce = FirebaseDatabase.getInstance().getReference("places").child(this.mPlaceKey);
        //this.placeImageAdapter = new PlaceImageAdapter(this, this.lstData);

        this.tvPlaceDetailAddress = (TextView)findViewById(R.id.tvPlaceDetailAddress);
        this.tvPlaceDetailDescription = (TextView)findViewById(R.id.tvPlaceDetailDescription);
        this.tvPlaceDetailTitle = (TextView)findViewById(R.id.tvPlaceDetailTitle);

        this.vpPlaceDetailImages = (ViewPager) findViewById(R.id.vpPlaceDetailImages);
        this.ciPlaceDetailImages = (CircleIndicator) findViewById(R.id.ciPlaceDetailImages);

        //this.ciPlaceDetailImages.setViewPager(this.vpPlaceDetailImages);

        //this.initSlideImage();

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.mPlaceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PlaceModel place = dataSnapshot.getValue(PlaceModel.class);
                if(place != null) {
                    latLngPlace = new LatLng(place.getLatitude(), place.getLongitude());
                    tvPlaceDetailTitle.setText(place.getTitle());
                    tvPlaceDetailAddress.setText(place.getAddress());
                    tvPlaceDetailDescription.setText(place.getDescription());

                    setMapMaker(place.getTitle(), latLngPlace);

                    placeImageAdapter = new PlaceImageAdapter(PlaceDetailActivity.this, place.getImagesAsList());
                    vpPlaceDetailImages.setAdapter(placeImageAdapter);
                    ciPlaceDetailImages.setViewPager(vpPlaceDetailImages);

                    initSlideImage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        this.mPlaceRefernce.addListenerForSingleValueEvent(this.mPlaceListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.mPlaceRefernce != null) {
            this.mPlaceRefernce.removeEventListener(this.mPlaceListener);
        }
    }
    
    private void initSlideImage() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == placeImageAdapter.getCount()) {
                    currentPage = 0;
                }
                vpPlaceDetailImages.setCurrentItem(currentPage++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 2500, 2500);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
            this.locationManager.removeUpdates(this);
            GoogleDirection.withServerKey(this.DIRECT_API_KEY)
                    .from(this.latLngCurrent)
                    .to(this.latLngPlace)
                    .unit(Unit.METRIC)
                    .execute(new DirectionCallback() {
                @Override
                public void onDirectionSuccess(Direction direction, String rawBody) {
                    if(direction.isOK()) {
                        List<Route> routes = direction.getRouteList();
                        Route route = routes.get(0);
                        List<Leg> legs = route.getLegList();
                        Leg leg = legs.get(0);
                        List<Step> steps = leg.getStepList();

                        Info distanceInfo = leg.getDistance();
                        Info durationInfo = leg.getDuration();


                        ArrayList<LatLng> directionPoint = leg.getDirectionPoint();
                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(PlaceDetailActivity.this, directionPoint, 5, Color.BLUE);
                        mMap.addPolyline(polylineOptions);
                    }
                }

                @Override
                public void onDirectionFailure(Throwable t) {

                }
            });

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
        this.mMap = googleMap;
        this.mMap.setMyLocationEnabled(true);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 10, this);
    }

    private void setMapMaker(String name, LatLng latLng) {
        MarkerOptions maker = new MarkerOptions();
        maker.position(latLng);
        maker.title(name);
        this.mMap.addMarker(maker);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
    }
}
