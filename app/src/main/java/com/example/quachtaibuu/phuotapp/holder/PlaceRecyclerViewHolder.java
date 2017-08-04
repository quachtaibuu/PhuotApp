package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quachtaibuu.phuotapp.R;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public class PlaceRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvItemNewsUserId;
    public TextView tvItemNewsCreated;
    public TextView tvItemNewsLocation;
    public TextView tvItemNewsTitle;
    public TextView tvItemNewsLike;
    public TextView tvItemNewsShare;
    public TextView tvItemNewsComment;
    public ImageView imgItemPlace;
    public ImageView imgItemUserImage;

    public Button btnItemNewsLike;
    public Button btnItemNewsComment;
    public Button btnItemNewsShare;

    public PlaceRecyclerViewHolder(View itemView) {
        super(itemView);
        this.tvItemNewsUserId = (TextView)itemView.findViewById(R.id.tvItemNewsUserId);
        this.tvItemNewsComment = (TextView)itemView.findViewById(R.id.tvItemNewsComment);
        this.tvItemNewsCreated = (TextView)itemView.findViewById(R.id.tvItemNewsCreated);
        this.tvItemNewsLike = (TextView)itemView.findViewById(R.id.tvItemNewsLike);
        this.tvItemNewsLocation = (TextView)itemView.findViewById(R.id.tvItemNewsLocation);
        this.tvItemNewsShare = (TextView)itemView.findViewById(R.id.tvItemNewsShare);
        this.tvItemNewsTitle = (TextView)itemView.findViewById(R.id.tvItemNewsTitle);
        this.imgItemPlace = (ImageView)itemView.findViewById(R.id.imgItemNewsPlace);
        this.imgItemUserImage = (ImageView)itemView.findViewById(R.id.imgItemUserImage);

        this.btnItemNewsComment = (Button)itemView.findViewById(R.id.btnItemNewsComment);
        this.btnItemNewsLike = (Button)itemView.findViewById(R.id.btnItemNewsLike);
        this.btnItemNewsShare = (Button)itemView.findViewById(R.id.btnItemNewsShare);
    }
}
