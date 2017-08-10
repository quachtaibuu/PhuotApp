package com.example.quachtaibuu.phuotapp;

import com.example.quachtaibuu.phuotapp.absFragement.AbsPlacesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Quach Tai Buu on 2017-08-10.
 */

public class TabMyPostFragement extends AbsPlacesFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference
                .child("user-places")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("created")
                .limitToFirst(100);
    }
}
