package com.example.quachtaibuu.phuotapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewHolder> {

    private List<PlaceModel> lstData = new ArrayList<>();

    public PlaceRecyclerViewAdapter(List<PlaceModel> data) {
        this.lstData = data;
    }

    @Override
    public PlaceRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_news, parent, false);
        return new PlaceRecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final PlaceRecyclerViewHolder holder, int position) {
        final PlaceModel placeModel = this.lstData.get(position);
        holder.imgItemUserImage.setImageResource(placeModel.getUserImage());
        holder.imgItemPlace.setImageResource(placeModel.getPlaceImage());
        holder.tvItemNewsTitle.setText(placeModel.getTitle());
        //holder.tvItemNewsShare.setText(placeModel.getGetCountShare());
        //holder.tvItemNewsLike.setText(placeModel.getCountLike());
        //        holder.tvItemNewsComment.setText(placeModel.getCountComment());
        holder.tvItemNewsLocation.setText(placeModel.getLocationName());
        holder.tvItemNewsCreated.setText(placeModel.getCreated());
        holder.tvItemNewsUserId.setText(placeModel.getUsername());

        holder.btnItemNewsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), String.format("Chia sẽ %s", placeModel.getTitle()), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnItemNewsLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), String.format("Đã thích %s", placeModel.getTitle()), Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnItemNewsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), String.format("Bình luận %s", placeModel.getTitle()), Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgItemPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), placeModel.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }
}
