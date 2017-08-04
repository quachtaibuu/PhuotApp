package com.example.quachtaibuu.phuotapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-07-31.
 */

public class LocationPagerAdapter extends FragmentPagerAdapter {

    public LocationPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    List<Fragment> mListFragment = new ArrayList<>();
    List<String> mListFragmentTitle = new ArrayList<>();

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mListFragmentTitle.get(position);
    }


    public void addFragment(Fragment fragment, String title) {
        mListFragment.add(fragment);
        mListFragmentTitle.add(title);
    }
}
