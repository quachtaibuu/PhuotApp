package com.example.quachtaibuu.phuotapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quachtaibuu.phuotapp.absFragement.AbsPlacesFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Quach Tai Buu on 2017-07-29.
 */

public class TabPopulateFragment extends AbsPlacesFragment {

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_tab_populate, container, false);
//        return rootView;
//    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("places").orderByChild("countLike").limitToFirst(100);
    }
}
