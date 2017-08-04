package com.example.quachtaibuu.phuotapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.LocationRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-07-31.
 */

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewHolder> {

    private List<LocationModel> lstData = new ArrayList<>();

    public LocationRecyclerViewAdapter(List<LocationModel> data) {
        this.lstData = data;
    }

    @Override
    public LocationRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contextView = inflater.inflate(R.layout.item_location, parent, false);
        return new LocationRecyclerViewHolder(contextView);
    }

    @Override
    public void onBindViewHolder(LocationRecyclerViewHolder holder, int position) {
        LocationModel locationModel = this.lstData.get(position);
        holder.imgItemLocation.setImageResource(locationModel.getImage());
        holder.tvItemLocationTitle.setText(locationModel.getName());
        holder.tvItemLocationDescription.setText(locationModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }
}
