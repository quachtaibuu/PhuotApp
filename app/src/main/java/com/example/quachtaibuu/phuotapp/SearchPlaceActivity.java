package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quachtaibuu.phuotapp.absFragement.AbsSearchActivity;
import com.example.quachtaibuu.phuotapp.adapter.BitmapRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.holder.ItemPlaceSmViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Quach Tai Buu on 2017-08-10.
 */

public class SearchPlaceActivity extends AbsSearchActivity {

    private Toolbar mToolBar;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private RecyclerView rcvSearchPlaceResult;
    private Query mQuery;

    private SearchPlaceUtil mSearchPlaceUtil = new SearchPlaceUtil();


    @Override
    public void onCreateFinish(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_tab_search);
        this.mToolBar = (Toolbar) findViewById(R.id.abSearchPlace);
        setSupportActionBar(this.mToolBar);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mQuery = this.mDatabase.child("places").orderByChild("title");

        this.mLayoutManager = new LinearLayoutManager(this);
        this.rcvSearchPlaceResult = (RecyclerView) findViewById(R.id.rcvSearchPlaceResult);
        this.rcvSearchPlaceResult.setLayoutManager(this.mLayoutManager);
        this.rcvSearchPlaceResult.addItemDecoration(new DividerItemDecoration(this, this.mLayoutManager.getOrientation()));
        this.rcvSearchPlaceResult.setHasFixedSize(true);
        this.rcvSearchPlaceResult.setAdapter(this.mAdapter);

        this.loadData(this.mQuery);
    }

    @Override
    public void doSearch(String strTextSearch) {
//        Query query = this.mDatabase.child("places")
//                .orderByChild("title")
//                .startAt(strTextSearch)
//                .endAt(strTextSearch + "\uf8ff")
//                .limitToFirst(100);
//        this.loadData(query);
        final List<PlaceModel> lst = this.mSearchPlaceUtil.search(strTextSearch);

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<ItemPlaceSmViewHolder>() {
            @Override
            public ItemPlaceSmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_place_sm, parent, false);
                return new ItemPlaceSmViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ItemPlaceSmViewHolder holder, int position) {
                final PlaceModel model = lst.get(position);
                holder.bindToPlace(model);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchPlaceActivity.this, PlaceDetailActivity.class);
                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, model.getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return lst.size();
            }
        };

        this.rcvSearchPlaceResult.setAdapter(adapter);

    }

    @Override
    public void loadData(Query query) {
        this.doSearch("");
    }

    //    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
//    @Override
//    public void loadData(Query query) {
//
//        if (this.mAdapter != null) {
//            this.mAdapter.cleanup();
//        }
//
//        if (query == null) {
//            query = this.mQuery;
//        }
//
//        this.mAdapter = new FirebaseRecyclerAdapter<PlaceModel, ItemPlaceSmViewHolder>(PlaceModel.class, R.layout.item_place_sm, ItemPlaceSmViewHolder.class, query) {
//
//            @Override
//            protected void populateViewHolder(ItemPlaceSmViewHolder viewHolder, PlaceModel model, int position) {
//                final DatabaseReference ref = getRef(position);
//
//                viewHolder.bindToPlace(model);
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(SearchPlaceActivity.this, PlaceDetailActivity.class);
//                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, ref.getKey());
//                        startActivity(intent);
//                    }
//                });
//            }
//
//        };
//
//        this.rcvSearchPlaceResult.setAdapter(this.mAdapter);
//
//    }

    public class SearchPlaceUtil {

        private List<PlaceModel> lstPlaces = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    PlaceModel model = data.getValue(PlaceModel.class);
                    model.setId(data.getKey());
                    lstPlaces.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        public SearchPlaceUtil() {
            this.loadData();
        }

        private void loadData() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("places")
                    .addListenerForSingleValueEvent(valueEventListener);

            ref.removeEventListener(valueEventListener);

        }


        public List<PlaceModel> search(@Nullable String strSearch) {
            //return this.lstPlaces.stream().filter(item -> item.getTitle().contains(strSearch)).collect(Collectors.toList());
            strSearch = strSearch.toLowerCase();
            List<PlaceModel> tmp = new ArrayList<>();
            for(PlaceModel model: this.lstPlaces) {
                if(model.getTitle().toLowerCase().contains(strSearch)) {
                    tmp.add(model);
                }
            }
            return tmp;
        }

    }

}
