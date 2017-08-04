package com.example.quachtaibuu.phuotapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.adapter.LocationRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.model.LocationModel;
import com.example.quachtaibuu.phuotapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-07-29.
 */

public class TabLocationFragment extends Fragment {

    private List<LocationModel> lstData = new ArrayList<>();
    private RecyclerView rcvListLocations;
    private LocationRecyclerViewAdapter locationAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_location, container, false);

        this.loadData();

        this.locationAdapter = new LocationRecyclerViewAdapter(this.lstData);
        this.layoutManager = new LinearLayoutManager(rootView.getContext());

        this.rcvListLocations = (RecyclerView)rootView.findViewById(R.id.rcvLocations);
        this.rcvListLocations.setHasFixedSize(true);
        this.rcvListLocations.setLayoutManager(this.layoutManager);
        this.rcvListLocations.setAdapter(this.locationAdapter);

        this.rcvListLocations.addOnItemTouchListener(new RecyclerItemClickListener(rootView.getContext(), this.rcvListLocations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LocationModel locationModel = lstData.get(position);
                Toast.makeText(rootView.getContext(), locationModel.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return rootView;
    }


    private void loadData() {
        lstData.add(new LocationModel("1", "An Giang", "Thất sơn huyền bí", R.drawable.loc_angiang));
        lstData.add(new LocationModel("2", "Bạc Liêu", "Xứ sở cơ cầu của Hắc công tử", R.drawable.loc_baclieu));
        lstData.add(new LocationModel("1", "An Giang", "Thất sơn huyền bí", R.drawable.loc_angiang));
        lstData.add(new LocationModel("2", "Bạc Liêu", "Xứ sở cơ cầu của Hắc công tử", R.drawable.loc_baclieu));
    }
}
