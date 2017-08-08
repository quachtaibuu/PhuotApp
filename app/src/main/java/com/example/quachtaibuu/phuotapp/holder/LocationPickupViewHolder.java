package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.model.LocationModel;

/**
 * Created by Quach Tai Buu on 2017-08-08.
 */

public class LocationPickupViewHolder extends RecyclerView.ViewHolder {

    private TextView tvItemSearchTitle;
    private TextView tvItemSearchDesceiption;

    public LocationPickupViewHolder(View itemView) {
        super(itemView);

        tvItemSearchTitle = (TextView) itemView.findViewById(R.id.tvItemSearchTitle);
        tvItemSearchDesceiption = (TextView) itemView.findViewById(R.id.tvItemSearchDescription);
    }

    public void bindToLocation(LocationModel model) {
        tvItemSearchTitle.setText(model.getName());
        tvItemSearchDesceiption.setText(model.getDescription());
    }
}
