package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.absFragement.AbsPlacesFragment;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.example.quachtaibuu.phuotapp.model.UserModel;
import com.example.quachtaibuu.phuotapp.utils.RecyclerItemClickListener;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TabNewsFeedFragment extends AbsPlacesFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("places").orderByChild("created").limitToFirst(100);
    }
}
