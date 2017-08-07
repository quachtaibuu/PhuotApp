package com.example.quachtaibuu.phuotapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationPickupActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private MenuItem mSearchAction;
    private EditText edSearch;
    private boolean isSearchIsOpened;

    private List<LocationModel> locationModels = new ArrayList<>();
    private ListView lvLocationPickupSearchResult;
    private LocationSearchAdapter locationSearchAdapter;

    private final DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("locations");
    private final Query QUERY = DATABASE_REFERENCE.orderByChild("name");
    private final ValueEventListener VALUE_EVENT_LISTENER = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            locationModels.clear();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                LocationModel locationModel = data.getValue(LocationModel.class);
                locationModel.setId(data.getKey());
                locationModels.add(locationModel);
            }
            locationSearchAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_pickup);

        this.mToolBar = (Toolbar)findViewById(R.id.customToolBar);
        setSupportActionBar(this.mToolBar);

        this.locationSearchAdapter = new LocationSearchAdapter(this, R.layout.item_search_location, this.locationModels);
        this.lvLocationPickupSearchResult = (ListView) findViewById(R.id.lvLocationPickupSearchResult);
        this.lvLocationPickupSearchResult.setAdapter(this.locationSearchAdapter);
        this.lvLocationPickupSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(LocationPickupActivity.this, AddNewPlaceActivity.class);
                intent.putExtra("location", new Gson().toJson(adapterView.getItemAtPosition(position)));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        this.loadData();
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
        if(this.isSearchIsOpened) {
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.edSearch.getWindowToken(), 0);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));
            this.isSearchIsOpened = false;
            this.loadData();
        }else {

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.search_bar);

            this.edSearch = (EditText)findViewById(R.id.edSearch);
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
                    this.handler.postDelayed(this.runnable, 500);
                }
            });

            this.edSearch.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(this.edSearch, InputMethodManager.SHOW_IMPLICIT);

            this.mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
            this.isSearchIsOpened = true;

        }
    }


    private void doSearch() {
        String strSearch = this.edSearch.getText().toString();
        this.QUERY.startAt(strSearch).endAt(strSearch + "\uF8FF").addListenerForSingleValueEvent(this.VALUE_EVENT_LISTENER);
    }


    private void loadData() {
        this.QUERY.addListenerForSingleValueEvent(this.VALUE_EVENT_LISTENER);
    }
}
