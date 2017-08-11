package com.example.quachtaibuu.phuotapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.adapter.LocationSearchAdapter;
import com.example.quachtaibuu.phuotapp.bus.LocationBus;
import com.example.quachtaibuu.phuotapp.holder.LocationPickupViewHolder;
import com.example.quachtaibuu.phuotapp.holder.LocationRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationPickupActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private MenuItem mSearchAction;
    private EditText edSearch;
    private boolean isSearchIsOpened;

    private RecyclerView rcvLocationsPikup;
    private LinearLayoutManager layoutManager;

    private final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("locations");
    private Query mPostQuery = mDatabaseReference.orderByChild("name");
    private FirebaseRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_pickup);

        this.mToolBar = (Toolbar) findViewById(R.id.customToolBar);
        setSupportActionBar(this.mToolBar);

        this.rcvLocationsPikup = (RecyclerView) findViewById(R.id.rcvLocationsPickup);

        this.layoutManager = new LinearLayoutManager(this);

        this.rcvLocationsPikup.setLayoutManager(this.layoutManager);
        this.rcvLocationsPikup.addItemDecoration(new DividerItemDecoration(this, this.layoutManager.getOrientation()));
        this.rcvLocationsPikup.setAdapter(this.mAdapter);
        this.loadData(this.mPostQuery);
    }


    public void loadData(Query query) {
        if (this.mAdapter != null) {
            this.mAdapter.cleanup();
        }

        this.mAdapter = new FirebaseRecyclerAdapter<LocationModel, LocationPickupViewHolder>(LocationModel.class, R.layout.item_search_location, LocationPickupViewHolder.class, query) {
            @Override
            protected void populateViewHolder(LocationPickupViewHolder viewHolder, final LocationModel model, int position) {
                DatabaseReference ref = getRef(position);
                model.setId(ref.getKey());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LocationPickupActivity.this, AddNewPlaceActivity.class);
                        intent.putExtra("location", new Gson().toJson(model));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

                viewHolder.bindToLocation(model);
            }
        };

        this.rcvLocationsPikup.setAdapter(this.mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleMenuSearch() {
        ActionBar actionBar = getSupportActionBar();
        if (this.isSearchIsOpened) {
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.edSearch.getWindowToken(), 0);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));
            this.isSearchIsOpened = false;
            this.loadData(this.mPostQuery);
        } else {

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.search_bar);

            this.edSearch = (EditText) findViewById(R.id.edSearch);
            this.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    doSearch();
                    return true;
                }
            });
            this.edSearch.addTextChangedListener(new TextWatcher() {

                private final Handler handler = new Handler();
                private Runnable runnable;

                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    this.handler.removeCallbacks(this.runnable);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    this.runnable = new Runnable() {
                        @Override
                        public void run() {
                            doSearch();
                        }
                    };
                    this.handler.postDelayed(this.runnable, 1000);
                }
            });

            this.edSearch.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(this.edSearch, InputMethodManager.SHOW_IMPLICIT);

            this.mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
            this.isSearchIsOpened = true;

        }
    }


    private void doSearch() {
        String strSearch = WordUtils.capitalize(this.edSearch.getText().toString().toLowerCase());
        Query mPostQuery = this.mPostQuery.startAt(strSearch).endAt(strSearch + "\uF8FF");
        this.loadData(mPostQuery);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this.mDatabaseReference.addListenerForSingleValueEvent(this.VALUE_EVENT_LISTENER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mAdapter.cleanup();
    }
}
