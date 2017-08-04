package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quachtaibuu.phuotapp.R;

/**
 * Created by Quach Tai Buu on 2017-07-31.
 */

public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgItemLocation;
    public TextView tvItemLocationTitle;
    public TextView tvItemLocationDescription;

    public LocationRecyclerViewHolder(View itemView) {
        super(itemView);
        this.imgItemLocation = (ImageView)itemView.findViewById(R.id.imgItemLocation);
        this.tvItemLocationTitle = (TextView)itemView.findViewById(R.id.tvItemLocationTitle);
        this.tvItemLocationDescription = (TextView)itemView.findViewById(R.id.tvItemLocationDescription);
    }
}
