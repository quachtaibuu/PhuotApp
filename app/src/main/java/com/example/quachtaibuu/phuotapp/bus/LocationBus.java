package com.example.quachtaibuu.phuotapp.bus;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-07.
 */

public class LocationBus {

    private DatabaseReference dbRef;
    private List<LocationModel> locationModelList = new ArrayList<>();
    private ArrayAdapter adapter;

    private final String DB_NODE = "locations";
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                LocationModel locationModel = data.getValue(LocationModel.class);
                locationModel.setId(data.getKey());
                locationModelList.add(locationModel);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public LocationBus() {}

    public LocationBus(ArrayAdapter adapter) {
        this.dbRef = FirebaseDatabase.getInstance().getReference(this.DB_NODE);
        this.adapter = adapter;
    }


    public List<LocationModel> getList() {
        this.locationModelList.clear();
        this.dbRef.orderByChild("name").addListenerForSingleValueEvent(this.valueEventListener);
        return this.locationModelList;
    }

    public List<LocationModel> getListSearchFor(String strQuery) {
        this.locationModelList.clear();
        this.dbRef.orderByChild("name")
                .startAt(strQuery)
                .endAt(strQuery + "\uf8ff")
                .addListenerForSingleValueEvent(this.valueEventListener);
        return this.locationModelList;
    }

}
