package com.example.quachtaibuu.phuotapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.adapter.LocationRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.example.quachtaibuu.phuotapp.utils.RecyclerItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-07-29.
 */

public class TabLocationFragment extends Fragment {

    private List<LocationModel> lstData = new ArrayList<>();
    private List<String> lstDataKey = new ArrayList<>();

    private RecyclerView rcvListLocations;
    private LocationRecyclerViewAdapter locationAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference databaseRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_location, container, false);

        this.locationAdapter = new LocationRecyclerViewAdapter(this.lstData);
        this.layoutManager = new LinearLayoutManager(rootView.getContext());

        this.rcvListLocations = (RecyclerView)rootView.findViewById(R.id.rcvLocations);
        this.rcvListLocations.setHasFixedSize(true);
        this.rcvListLocations.setLayoutManager(this.layoutManager);
        this.rcvListLocations.setAdapter(this.locationAdapter);

        this.rcvListLocations.addOnItemTouchListener(new RecyclerItemClickListener(rootView.getContext(), this.rcvListLocations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LocationModel locationModel = lstData.get(position);
                Toast.makeText(rootView.getContext(), locationModel.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        this.databaseRef = FirebaseDatabase.getInstance().getReference().child("locations");
        this.databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(lstDataKey.indexOf(key) == -1) {
                    LocationModel location = dataSnapshot.getValue(LocationModel.class);
                    lstDataKey.add(key);
                    lstData.add(location);
                    locationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                int index = lstDataKey.indexOf(key);
                if(index != -1) {
                    LocationModel location = dataSnapshot.getValue(LocationModel.class);
                    lstData.set(index, location);
                    locationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = lstDataKey.indexOf(key);
                if(index != -1) {
                    lstDataKey.remove(index);
                    lstData.remove(index);
                    locationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
