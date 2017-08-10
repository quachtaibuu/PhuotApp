package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quachtaibuu.phuotapp.adapter.LocationPagerAdapter;


public class PlaceFragment extends Fragment {

    private TabLayout mTab;
    private View mView;
    private ViewPager mViewPager;
    private FloatingActionButton mFabAddNewPlace;
    private View.OnClickListener mFabAddNewPlace_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), AddNewPlaceActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setReenterTransition(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mView = (View)inflater.inflate(R.layout.fragment_place, container, false);
        this.mTab = (TabLayout) this.mView.findViewById(R.id.tabs);
        this.mViewPager = (ViewPager) this.mView.findViewById(R.id.vpLocationContainer);
        this.mFabAddNewPlace = (FloatingActionButton)this.mView.findViewById(R.id.fabAddNewPlace);
        this.mFabAddNewPlace.setOnClickListener(this.mFabAddNewPlace_OnClick);

        this.setupViewPager();
        this.mTab.setupWithViewPager(this.mViewPager);

        return this.mView;
    }

    private void setupViewPager() {
        LocationPagerAdapter locationPagerAdapter = new LocationPagerAdapter(getChildFragmentManager());
        locationPagerAdapter.addFragment(new TabNewsFeedFragment(), getResources().getString(R.string.tabNewsFeed));
        locationPagerAdapter.addFragment(new TabPopulateFragment(), getResources().getString(R.string.tabPopulate));
        locationPagerAdapter.addFragment(new TabLocationFragment(), getResources().getString(R.string.tabLocation));
        locationPagerAdapter.addFragment(new TabMyPostFragement(), getResources().getString(R.string.tabMyPosted));
        this.mViewPager.setAdapter(locationPagerAdapter);
    }
}
