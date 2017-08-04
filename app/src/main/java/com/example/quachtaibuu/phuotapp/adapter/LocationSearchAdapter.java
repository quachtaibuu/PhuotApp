package com.example.quachtaibuu.phuotapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.model.LocationModel;

import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-03.
 */

public class LocationSearchAdapter extends ArrayAdapter<LocationModel> {

    private TextView tvItemSearchTitle;
    private TextView tvItemSearchDesceiption;

    private Context context;
    private int resource;
    private List<LocationModel> objects;


    public LocationSearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<LocationModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_search_location, null, false);
        this.tvItemSearchTitle = (TextView) rootView.findViewById(R.id.tvItemSearchTitle);
        this.tvItemSearchDesceiption = (TextView) rootView.findViewById(R.id.tvItemSearchDescription);
        LocationModel locationModel = this.objects.get(position);

        this.tvItemSearchTitle.setText(locationModel.getName());
        this.tvItemSearchDesceiption.setText(locationModel.getDescription());

        return rootView;
    }
}
