package com.example.quachtaibuu.phuotapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.Query;

import org.apache.commons.lang3.text.WordUtils;

public class TabSearchFragment extends Fragment {

    private AppCompatActivity activity;

    private Toolbar mToolBar;
    private EditText edSearch;
    private boolean isSearchIsOpened;

    private MenuItem mSearchAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_search, container, false);
//
//        this.mToolBar = (Toolbar)rootView.findViewById(R.id.customToolBar);
//
//
//        return rootView;

        setHasOptionsMenu(true);
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_Light_NoActionBar);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        this.activity = (AppCompatActivity)getActivity();
        this.activity.setSupportActionBar(this.mToolBar);
        this.mToolBar = (Toolbar)rootView.findViewById(R.id.abSearchPlace);

        this.activity = (AppCompatActivity)getActivity();
        this.activity.setSupportActionBar(this.mToolBar);

        return localInflater.inflate(R.layout.fragment_tab_search, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        this.mSearchAction = menu.findItem(R.id.action_search);
        //super.onCreateOptionsMenu(menu, inflater);
        return;
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
        ActionBar actionBar = this.activity.getSupportActionBar();
        if (this.isSearchIsOpened) {
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.edSearch.getWindowToken(), 0);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_24dp));
            this.isSearchIsOpened = false;
            //this.loadData(this.mPostQuery);
        } else {

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.search_bar);

            this.edSearch = (EditText) this.activity.findViewById(R.id.edSearch);
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
            InputMethodManager inputMethodManager = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(this.edSearch, InputMethodManager.SHOW_IMPLICIT);

            this.mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_black_24dp));
            this.isSearchIsOpened = true;

        }
    }


    private void doSearch() {
//        String strSearch = WordUtils.capitalize(this.edSearch.getText().toString().toLowerCase());
//        Query mPostQuery = this.mPostQuery.startAt(strSearch).endAt(strSearch + "\uF8FF");
//        this.loadData(mPostQuery);
    }
}
