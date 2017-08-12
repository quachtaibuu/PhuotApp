package com.example.quachtaibuu.phuotapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
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
import com.example.quachtaibuu.phuotapp.utils.PhuotAppDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class PlaceDetailActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    //private final String DIRECT_API_KEY = "AIzaSyDMQ7SIMMaCQ1qjPDwNbSO5MEp7lu2-ctg";
    public static final String EXTRA_PLACE_KEY = "place_key";

    private int currentPage = 0;
    private ViewPager vpPlaceDetailImages;
    private PlaceImageAdapter placeImageAdapter;
    private CircleIndicator ciPlaceDetailImages;
    private TextView tvPlaceDetailTitle;
    private TextView tvPlaceDetailAddress;
    private TextView tvPlaceDetailDescription;
    private TextView tvPlaceDetailDistance;
    private TextView tvPlaceDetailDuranction;
    private ImageView btnPlaceDetailBookmark;

    private PlaceModel placeModel;
    private GoogleMap mMap;

    private LatLng latLngPlace;
    private LatLng latLngCurrent;

    private LocationManager locationManager;
    private DatabaseReference mDatabase;
    private String mPlaceKey;
    private DatabaseReference mPlaceRefernce;
    private ValueEventListener mPlaceListener;

    private View.OnClickListener btnPlaceDetailBookmark_OnClick = new View.OnClickListener() {

        private Task<DataSnapshot> updateBookmark(final DatabaseReference ref) {

            final TaskCompletionSource<DataSnapshot> tcs = new TaskCompletionSource<>();

            ref.runTransaction(new Transaction.Handler() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {

                    final PlaceModel place = mutableData.getValue(PlaceModel.class);

                    if (place == null) {
                        return Transaction.success(mutableData);
                    }

                    if (place.getBookmarks().containsKey(getUserId())) {
                        place.setCountBookmark(place.getCountBookmark() - 1);
                        place.getBookmarks().remove(getUserId());
                    } else {
                        place.setCountBookmark(place.getCountBookmark() + 1);
                        place.getBookmarks().put(getUserId(), true);
                    }

                    mutableData.setValue(place);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    if (b) {
                        tcs.setResult(dataSnapshot);
                    } else {
                        tcs.setException(new IllegalArgumentException(databaseError.getMessage()));
                    }
                }
            });

            return tcs.getTask();
        }

        private Task<DataSnapshot> modifiedBookmark(final DatabaseReference ref) {

            final TaskCompletionSource<DataSnapshot> taskCompletionSource = new TaskCompletionSource<>();

            DatabaseReference placeRef = mDatabase.child(PhuotAppDatabase.PLACES).child(ref.getKey());
            placeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    PlaceModel place = dataSnapshot.getValue(PlaceModel.class);
                    if(place.getBookmarks().containsKey(getUserId())) {
                        ref.setValue(place);
                    }else {
                        ref.removeValue();
                    }
                    taskCompletionSource.setResult(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    taskCompletionSource.setException(new DatabaseException(databaseError.getMessage()));
                }
            });

            return taskCompletionSource.getTask();

        }

        @Override
        public void onClick(View view) {
            DatabaseReference userBookmarksRef = mDatabase.child(PhuotAppDatabase.USER_BOOKMARKS).child(getUserId()).child(mPlaceKey);
            DatabaseReference placesRef = mDatabase.child(PhuotAppDatabase.PLACES).child(mPlaceKey);
            DatabaseReference userPlacesRef = mDatabase.child(PhuotAppDatabase.USER_PLACES).child(placeModel.getUser().getUserKey()).child(mPlaceKey);
            DatabaseReference locationPlacesRef = mDatabase.child(PhuotAppDatabase.LOCATION_PLACES).child(placeModel.getLocation().getLocationKey()).child(mPlaceKey);

            Task<DataSnapshot> updateTasks[] = new Task[]{
                    updateBookmark(placesRef),
                    updateBookmark(userPlacesRef),
                    updateBookmark(locationPlacesRef),
                    modifiedBookmark(userBookmarksRef)
            };

            Tasks.whenAll(updateTasks).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(PlaceDetailActivity.this, getString(R.string.msgSuccessBookmarkSaved), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PlaceDetailActivity.this, "Bookmark: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        this.mPlaceKey = getIntent().getStringExtra(this.EXTRA_PLACE_KEY);
        if (this.mPlaceKey == null) {
            throw new IllegalArgumentException("Key not found!");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlaceDetail);
        mapFragment.getMapAsync(this);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mPlaceRefernce = this.mDatabase.child("places").child(this.mPlaceKey);
        //this.placeImageAdapter = new PlaceImageAdapter(this, this.lstData);

        this.tvPlaceDetailAddress = (TextView) findViewById(R.id.tvPlaceDetailAddress);
        this.tvPlaceDetailDescription = (TextView) findViewById(R.id.tvPlaceDetailDescription);
        this.tvPlaceDetailTitle = (TextView) findViewById(R.id.tvPlaceDetailTitle);
        this.tvPlaceDetailDistance = (TextView) findViewById(R.id.tvPlaceDetailDistance);
        this.tvPlaceDetailDuranction = (TextView) findViewById(R.id.tvPlaceDetailDuration);

        this.btnPlaceDetailBookmark = (ImageView) findViewById(R.id.btnPlaceDetailBookmark);
        this.btnPlaceDetailBookmark.setOnClickListener(this.btnPlaceDetailBookmark_OnClick);

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

                placeModel = dataSnapshot.getValue(PlaceModel.class);

                if (placeModel != null) {

                    if (placeModel.getBookmarks().containsKey(getUserId())) {
                        btnPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    } else {
                        btnPlaceDetailBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    }

                    LatLng latLngCheck = new LatLng(placeModel.getLatitude(), placeModel.getLongitude());

                    if (latLngCurrent != null && latLngCheck != latLngPlace) {
                        getDirection(latLngCurrent, latLngPlace);
                    }

                    latLngPlace = latLngCheck;

                    tvPlaceDetailTitle.setText(placeModel.getTitle());
                    tvPlaceDetailAddress.setText(placeModel.getAddress());
                    tvPlaceDetailDescription.setText(placeModel.getDescription());

                    setMapMaker(placeModel.getTitle(), latLngPlace);

                    placeImageAdapter = new PlaceImageAdapter(PlaceDetailActivity.this, placeModel.getImages());
                    vpPlaceDetailImages.setAdapter(placeImageAdapter);
                    ciPlaceDetailImages.setViewPager(vpPlaceDetailImages);

                    initSlideImage();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        this.mPlaceRefernce.addValueEventListener(this.mPlaceListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mPlaceRefernce != null) {
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

    private void getDirection(LatLng latLngCurrent, LatLng latLngPlace) {

        GoogleDirection.withServerKey(getString(R.string.gg_direct_api_key))
                .from(latLngCurrent)
                .to(latLngPlace)
                .unit(Unit.METRIC)
                .language(Language.VIETNAMESE)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
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

                            tvPlaceDetailDistance.setText(distanceInfo.getText());
                            tvPlaceDetailDuranction.setText(durationInfo.getText());

                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(PlaceDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
            getDirection(latLngCurrent, latLngPlace);

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
