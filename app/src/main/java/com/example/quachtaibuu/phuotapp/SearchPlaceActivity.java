package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.quachtaibuu.phuotapp.absFragement.AbsSearchActivity;
import com.example.quachtaibuu.phuotapp.holder.ItemPlaceSmViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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




    @Override
    public void onCreateFinish(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_tab_search);
        this.mToolBar = (Toolbar) findViewById(R.id.abSearchPlace);
        setSupportActionBar(this.mToolBar);

        this.mDatabase = FirebaseDatabase.getInstance().getReference("places");
        this.mQuery = this.mDatabase.orderByChild("title");

        this.mLayoutManager = new LinearLayoutManager(this);
        this.rcvSearchPlaceResult = (RecyclerView)findViewById(R.id.rcvSearchPlaceResult);
        this.rcvSearchPlaceResult.setLayoutManager(this.mLayoutManager);
        this.rcvSearchPlaceResult.addItemDecoration(new DividerItemDecoration(this, this.mLayoutManager.getOrientation()));
        this.rcvSearchPlaceResult.setHasFixedSize(true);
        this.rcvSearchPlaceResult.setAdapter(this.mAdapter);

        this.loadData(this.mQuery);
    }

    @Override
    public void doSearch(String strTextSearch) {
        Query query = this.mDatabase.orderByChild("title").startAt(strTextSearch).endAt(strTextSearch + "\uF8FF").limitToFirst(100);
        this.loadData(query);
    }

    @Override
    public void loadData(Query query) {

        if(this.mAdapter != null) {
            this.mAdapter.cleanup();
        }

        if(query == null) {
            query = this.mQuery;
        }

        this.mAdapter = new FirebaseRecyclerAdapter<PlaceModel, ItemPlaceSmViewHolder>(PlaceModel.class, R.layout.item_place_sm, ItemPlaceSmViewHolder.class, query) {

            @Override
            protected void populateViewHolder(ItemPlaceSmViewHolder viewHolder, PlaceModel model, int position) {
                final DatabaseReference ref = getRef(position);

                viewHolder.bindToPlace(model);

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchPlaceActivity.this, PlaceDetailActivity.class);
                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, ref.getKey());
                        startActivity(intent);
                    }
                });
            }

        };

        this.rcvSearchPlaceResult.setAdapter(this.mAdapter);

    }

}
