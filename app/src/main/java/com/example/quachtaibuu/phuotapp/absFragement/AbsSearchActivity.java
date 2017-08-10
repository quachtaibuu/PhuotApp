package com.example.quachtaibuu.phuotapp.absFragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.TextView;

import com.example.quachtaibuu.phuotapp.AddNewPlaceActivity;
import com.example.quachtaibuu.phuotapp.LocationPickupActivity;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.LocationPickupViewHolder;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Quach Tai Buu on 2017-08-10.
 */

public abstract class AbsSearchActivity extends AppCompatActivity {

    private MenuItem mSearchAction;
    private EditText edSearch;
    private boolean isSearchIsOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onCreateFinish(savedInstanceState);
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
            this.loadData();
        } else {

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.search_bar);

            this.edSearch = (EditText) findViewById(R.id.edSearch);
            this.edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    doSearch(edSearch.getText().toString());
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
                            doSearch(edSearch.getText().toString());
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


    public abstract void onCreateFinish(Bundle savedInstanceState);
    public abstract void loadData();
    public abstract void doSearch(String strTextSearch);

}
