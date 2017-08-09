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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private final String DIRECT_API_KEY = "AIzaSyDjXHNyOW7EZU42ubxtQ8RgYQZTyzf6_EA";
    public static final String EXTRA_PLACE_KEY = "place_key";

    private int currentPage = 0;
    private ViewPager vpPlaceDetailImages;
    private PlaceImageAdapter placeImageAdapter;
    private List<Integer> lstData = new ArrayList<>();
    private CircleIndicator ciPlaceDetailImages;
    private GoogleMap mMap;

    private PlaceModel placeModel;
    private LatLng latLngPlace;
    private LatLng latLngCurrent;

    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlaceDetail);
        mapFragment.getMapAsync(this);

        this.loadData();

        this.vpPlaceDetailImages = (ViewPager) findViewById(R.id.vpPlaceDetailImages);
        this.vpPlaceDetailImages.setAdapter(new PlaceImageAdapter(this, this.lstData));
        this.ciPlaceDetailImages = (CircleIndicator) findViewById(R.id.ciPlaceDetailImages);
        this.ciPlaceDetailImages.setViewPager(this.vpPlaceDetailImages);

        this.initSlideImage();

    }

    private void loadData() {
//        this.placeModel = new PlaceModel("Miếu bà chúa xứ Núi Sam", R.drawable.news_avatar, "Góc Thư Giản", "1 tháng 8 lúc 03:11 SA", "An Giang", R.drawable.pc_mieuba, 6, 4, 2, 10.6795326, 105.0763105);
//
//        this.latLngPlace = new LatLng(this.placeModel.getLatitude(), this.placeModel.getLongitude());
//
//        this.lstData.add(R.drawable.loc_angiang);
//        this.lstData.add(R.drawable.loc_baclieu);
    }

    private void initSlideImage() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == lstData.size()) {
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
        MarkerOptions maker = new MarkerOptions();
        maker.position(this.latLngPlace);
        maker.title(this.placeModel.getTitle());
        this.mMap.addMarker(maker);
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.latLngPlace, 7));
        this.mMap.setMyLocationEnabled(true);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 10, this);
    }
}
