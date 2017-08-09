package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.example.quachtaibuu.phuotapp.model.UserModel;
import com.example.quachtaibuu.phuotapp.utils.RecyclerItemClickListener;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TabNewsFeedFragment extends Fragment {

    private RecyclerView rcvNewsFeeds;
    private List<PlaceModel> placeModels = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_news_feed, container, false);

        //this.loadData();
        this.mDatabase = FirebaseDatabase.getInstance().getReference("places");
        Query postQuery = this.mDatabase.orderByChild("created");

        this.mAdapter = new FirebaseRecyclerAdapter<PlaceModel, PlaceRecyclerViewHolder>(PlaceModel.class, R.layout.item_news, PlaceRecyclerViewHolder.class, postQuery) {

            @Override
            protected void populateViewHolder(final PlaceRecyclerViewHolder viewHolder, final PlaceModel model, int position) {
                final DatabaseReference ref = getRef(position);


                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        UserModel userModel = mutableData.child("user").getValue(UserModel.class);
                        LocationModel locationModel = mutableData.child("location").getValue(LocationModel.class);
                        model.setUser(userModel);
                        model.setLocation(locationModel);
                        return null;
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        viewHolder.bindToPlace(model);
                    }
                });

            }
        };

        this.layoutManager = new LinearLayoutManager(rootView.getContext());
        //this.placeRecyclerViewAdapter = new PlaceRecyclerViewAdapter(this.placeModels);
        this.rcvNewsFeeds = (RecyclerView)rootView.findViewById(R.id.rcvNewsFeeds);
        this.rcvNewsFeeds.setHasFixedSize(true);
        this.rcvNewsFeeds.setLayoutManager(this.layoutManager);
        this.rcvNewsFeeds.setAdapter(this.mAdapter);

//        this.rcvNewsFeeds.addOnItemTouchListener(new RecyclerItemClickListener(rootView.getContext(), this.rcvNewsFeeds, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(view.getContext(), placeModels.get(position).toString(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(view.getContext(), PlaceDetailActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        }));

        return rootView;
    }
}
