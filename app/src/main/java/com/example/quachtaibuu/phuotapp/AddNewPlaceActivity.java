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
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quachtaibuu.phuotapp.adapter.BitmapRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.example.quachtaibuu.phuotapp.utils.PhuotAppDatabase;
import com.example.quachtaibuu.phuotapp.utils.UserSessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddNewPlaceActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_LOCATION_REQUEST = 10;
    public static final int PICK_PLACE_REQUEST = 100;

    private GoogleMap mMapPlace;
    private LocationManager locationManager;
    private LatLng currentLatLng;
    private TextView tvAddNewPlaceLocationPickupName;
    private EditText edAddNewPlaceName;
    private EditText edAddNewPlaceAddress;
    private EditText edAddNewPlaceDescription;
    private ImageView imgAddNewPlaceChooseImage;
    private List<Uri> lstUriImageChoosen = new ArrayList<>();
    private RecyclerView rcvAddNewPlacePictures;
    private BitmapRecyclerViewAdapter bitmapRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private LocationModel locationModelPickup;
    private DatabaseReference mDatabase;
    private Marker mMarker;

    private PlaceModel mPlaceModel;
    private UserSessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlace);
        mapFragment.getMapAsync(this);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        this.tvAddNewPlaceLocationPickupName = (TextView) findViewById(R.id.tvAddNewPlaceLocationPickupName);

        this.edAddNewPlaceAddress = (EditText) findViewById(R.id.edAddNewPlaceAddress);
        this.edAddNewPlaceDescription = (EditText) findViewById(R.id.edAddNewPlaceDescription);
        this.edAddNewPlaceName = (EditText) findViewById(R.id.edAddNewPlaceName);

        this.bitmapRecyclerViewAdapter = new BitmapRecyclerViewAdapter();
        this.bitmapRecyclerViewAdapter.setOnButtonClickListener(new BitmapRecyclerViewAdapter.OnButtonClickListener() {
            @Override
            public void onRemoveClick(int postion) {
                lstUriImageChoosen.remove(postion);
                loadData();
            }
        });
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);

        this.rcvAddNewPlacePictures = (RecyclerView) findViewById(R.id.rcvAddNewPlacePictures);
        this.rcvAddNewPlacePictures.setHasFixedSize(true);
        this.rcvAddNewPlacePictures.setLayoutManager(this.layoutManager);

        this.getPlaceData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_place, menu);
        if (this.mPlaceModel == null) {
            MenuItem actionAdd = menu.findItem(R.id.action_place_add);
            actionAdd.setVisible(true);
        } else {
            MenuItem actionEdit = menu.findItem(R.id.action_place_edit);
            actionEdit.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_place_add:

                if(!this.isValidateInput()) {
                    return false;
                }

                showProgressDialog();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference();
                final List<String> images = new ArrayList<>();


                for (Uri imgUri : lstUriImageChoosen) {
                    final String imgName = imgUri.getLastPathSegment();
                    final String imgPath = String.format("/img_places/%s_%s", new Date().getTime(), imgName);

                    StorageReference ref = storageRef.child(imgPath);
                    UploadTask uploadTask = ref.putFile(imgUri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            images.add(imgPath);
                            if (images.size() == lstUriImageChoosen.size()) {

                                DatabaseReference reference = mDatabase.child("places");
                                String key = reference.push().getKey();

                                PlaceModel placeModel = setInputToModel(new PlaceModel());
                                placeModel.setImages(images);

                                Map<String, Object> placeValues = placeModel.toMap();

                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("/places/" + key, placeModel.toMap());
                                childUpdates.put("/user-places/" + getUserId() + "/" + key, placeValues);
                                childUpdates.put("/location-places/" + locationModelPickup.getLocationKey() + "/" + key, placeValues);

                                mDatabase.updateChildren(childUpdates);

                                Toast.makeText(AddNewPlaceActivity.this, getString(R.string.msgSuccessInsertedForPlace), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewPlaceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            hideProgressDialog();
                        }
                    });
                }

                break;

            case R.id.action_place_edit:

                if(!this.isValidateInput()) {
                    return false;
                }

                UploadImageHelper.MultipleUpload(this.lstUriImageChoosen, new UploadImageHelper.UploadImageStatus() {

                    @Override
                    public void OnProcessing() {
                        showProgressDialog();
                    }

                    @Override
                    public void OnSuccess(List<String> images) {

                        String placeKey = mPlaceModel.getId();
                        String locationKey = mPlaceModel.getLocation().getLocationKey();
                        String userKey = mPlaceModel.getUser().getUserKey();
                        setInputToModel(mPlaceModel);
                        mPlaceModel.setImages(mPlaceModel.getImages());
                        if(images.size() > 0) {
                            mPlaceModel.getImages().addAll(images);
                        }

                        DatabaseReference placeRef = mDatabase.child(PhuotAppDatabase.PLACES).child(placeKey);
                        DatabaseReference userPlaceRef = mDatabase.child(PhuotAppDatabase.USER_PLACES).child(userKey).child(placeKey);
                        DatabaseReference userBookmarkRef = mDatabase.child(PhuotAppDatabase.USER_BOOKMARKS).child(userKey).child(placeKey);
                        DatabaseReference locationPlaceRef = mDatabase.child(PhuotAppDatabase.LOCATION_PLACES).child(locationKey).child(placeKey);

                        Task<?>[] doUpdateTask = new Task[]{
                                updatePlace(placeRef, mPlaceModel),
                                updatePlace(userPlaceRef, mPlaceModel),
                                updatePlace(userBookmarkRef, mPlaceModel),
                                locationPlaceRef.removeValue(),
                                locationPlaceRef.setValue(mPlaceModel.toMap())
                        };

                        Tasks.whenAll(doUpdateTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddNewPlaceActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewPlaceActivity.this, "Không thể cập nhật thông tin!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void OnError(Exception e) {
                        Toast.makeText(AddNewPlaceActivity.this, "Images: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnComplete() {
                        hideProgressDialog();
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private Task<DataSnapshot> updatePlace(DatabaseReference ref, final PlaceModel newPlace) {

        final TaskCompletionSource<DataSnapshot> tcs = new TaskCompletionSource();

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                PlaceModel model = mutableData.getValue(PlaceModel.class);
                if (model == null) {
                    return Transaction.success(mutableData);
                }

                model.setPlaceModel(newPlace);

                mutableData.setValue(model);

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

    private boolean isValidateInput() {
        String errorRequired = getString(R.string.msgErrorInputRequired);

        String title = edAddNewPlaceName.getText().toString();
        String address = edAddNewPlaceAddress.getText().toString();
        String description = edAddNewPlaceDescription.getText().toString();

        if(title.isEmpty())
        {
            edAddNewPlaceName.setError(errorRequired);
            edAddNewPlaceName.requestFocus();
            return false;
        }

        if(this.locationModelPickup == null) {
            Toast.makeText(this, getString(R.string.msgErrorLocationPickupRequired), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(this.currentLatLng == null) {
            Toast.makeText(this, getString(R.string.msgErrorMapPickupRequired), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(address.isEmpty()) {
            edAddNewPlaceAddress.setError(errorRequired);
            edAddNewPlaceAddress.requestFocus();
            return false;
        }

        if(description.isEmpty()) {
            edAddNewPlaceDescription.setError(errorRequired);
            edAddNewPlaceDescription.requestFocus();
            return false;
        }

        if(this.lstUriImageChoosen.size() == 0 && this.mPlaceModel == null) {
            Toast.makeText(this, getString(R.string.msgErrorImagePickupRequest), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private PlaceModel setInputToModel(PlaceModel placeModel) {

        String title = edAddNewPlaceName.getText().toString();
        String address = edAddNewPlaceAddress.getText().toString();
        String description = edAddNewPlaceDescription.getText().toString();
        double latitude = this.currentLatLng.latitude;
        double longtitude = this.currentLatLng.longitude;

        if (title.isEmpty()) {

            this.edAddNewPlaceName.requestFocus();
        }

        placeModel.setAddress(address);
        placeModel.setDescription(description);
        placeModel.setTitle(title);
        placeModel.setLatitude(latitude);
        placeModel.setLongitude(longtitude);
        //placeModel.setImages(images);
        placeModel.setLocation(locationModelPickup);

        if(this.mPlaceModel == null) {
            placeModel.setUser(this.getCurrentUser());
        }

        return placeModel;

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && this.mPlaceModel == null) {
            setLocationMaker(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        if (this.currentLatLng != null) {
            this.locationManager.removeUpdates(this);
        }
    }

    private void setLocationMaker(LatLng latLng) {

        if(latLng == null || this.mMapPlace == null)
        {
            return;
        }

        try {

            if (this.mMarker != null) {
                this.mMarker.remove();
            }

            this.currentLatLng = latLng;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            this.mMarker = this.mMapPlace.addMarker(markerOptions);
            this.mMapPlace.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            //String city = address.getLocality().replace("City", "");
            this.edAddNewPlaceAddress.setText(address.getAddressLine(0));
            //this.tvAddNewPlaceLocationPickupName.setText(city);
        } catch (IOException e) {
            e.printStackTrace();
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

        this.setLocationMaker(this.currentLatLng);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == this.PICK_PLACE_REQUEST) {
                this.currentLatLng = new Gson().fromJson(data.getStringExtra("latLngPickup"), LatLng.class);
                this.setLocationMaker(this.currentLatLng);
            } else if (requestCode == this.PICK_IMAGE_REQUEST) {
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
            } else if (requestCode == this.PICK_LOCATION_REQUEST) {
                this.locationModelPickup = new Gson().fromJson(data.getStringExtra("location"), LocationModel.class);
                this.setLocationPickup();
            }
        }
    }

    public void tvAddNewPlaceChoosenLocation_OnClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("currentLatLng", new Gson().toJson(currentLatLng));
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
        if (itemCount > 0) {
            this.rcvAddNewPlacePictures.smoothScrollToPosition(itemCount - 1);
            this.layoutManager.scrollToPosition(itemCount - 1);
        }
    }

    private void addImageUri(Uri imgUri) {
        if (this.lstUriImageChoosen.indexOf(imgUri) != -1) {
            return;
        }
        this.lstUriImageChoosen.add(imgUri);
    }


    private void setLocationPickup() {
        this.tvAddNewPlaceLocationPickupName.setText(this.locationModelPickup.getName());
    }


    private void getPlaceData() {
        Intent intent = getIntent();
        if (intent != null) {
            //String jsonPlace = intent.getStringExtra("place");
            final String placeKey = intent.getStringExtra("placeKey");

            if (placeKey != null) {

                this.mDatabase.child(PhuotAppDatabase.PLACES).child(placeKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mPlaceModel = dataSnapshot.getValue(PlaceModel.class);

                        if (mPlaceModel == null) {
                            return;
                        }

                        mPlaceModel.setId(placeKey);
                        edAddNewPlaceAddress.setText(mPlaceModel.getAddress());
                        edAddNewPlaceDescription.setText(mPlaceModel.getDescription());
                        edAddNewPlaceName.setText(mPlaceModel.getTitle());
                        currentLatLng = new LatLng(mPlaceModel.getLatitude(), mPlaceModel.getLongitude());

                        locationModelPickup = mPlaceModel.getLocation();

                        setLocationPickup();
                        setLocationMaker(currentLatLng);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    public static class UploadImageHelper {

        public interface UploadImageStatus {
            void OnProcessing();

            void OnSuccess(List<String> images);

            void OnError(Exception e);

            void OnComplete();
        }

        public static void MultipleUpload(final List<Uri> images, final UploadImageStatus uploadStatus) {

            uploadStatus.OnProcessing();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            final List<String> listImageUpload = new ArrayList<>();

            if (images.size() == 0) {
                uploadStatus.OnSuccess(new ArrayList<String>());
            }

            for (Uri imgUri : images) {

                final String imgName = imgUri.getLastPathSegment();
                final String imgPath = String.format("/img_places/%s_%s", new Date().getTime(), imgName);

                UploadTask uploadTask = storageRef.child(imgPath).putFile(imgUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        listImageUpload.add(imgPath);
                        if (images.size() == listImageUpload.size()) {
                            uploadStatus.OnSuccess(listImageUpload);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        uploadStatus.OnError(e);
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        uploadStatus.OnComplete();
                    }
                });
            }

        }

    }
}
