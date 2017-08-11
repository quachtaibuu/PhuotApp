package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public class PlaceRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvItemNewsUserId;
    public TextView tvItemNewsCreated;
    public TextView tvItemNewsLocation;
    public TextView tvItemNewsTitle;
    public TextView tvItemNewsLike;
    public TextView tvItemNewsComment;

    public ImageView imgItemPlace;
    public ImageView imgItemUserImage;

    public Button btnItemNewsLike;
    public Button btnItemNewsComment;
    public Button btnItemNewsEdit;

    public interface OnButtonClickListenser {
        void ButtonLike_OnClick(View view);

        void ButtonComment_OnClick(View view);

        void ButtonEdit_OnClick(View view);
    }

    public PlaceRecyclerViewHolder(View itemView) {
        super(itemView);
        this.tvItemNewsUserId = (TextView) itemView.findViewById(R.id.tvItemNewsUserId);
        this.tvItemNewsCreated = (TextView) itemView.findViewById(R.id.tvItemNewsCreated);
        this.tvItemNewsLike = (TextView) itemView.findViewById(R.id.tvItemNewsLike);
        this.tvItemNewsComment = (TextView) itemView.findViewById(R.id.tvItemNewsComment);
        this.tvItemNewsLocation = (TextView) itemView.findViewById(R.id.tvItemNewsLocation);
        this.tvItemNewsTitle = (TextView) itemView.findViewById(R.id.tvItemNewsTitle);
        this.imgItemPlace = (ImageView) itemView.findViewById(R.id.imgItemNewsPlace);
        this.imgItemUserImage = (ImageView) itemView.findViewById(R.id.imgItemUserImage);

        this.btnItemNewsLike = (Button) itemView.findViewById(R.id.btnItemNewsLike);
        this.btnItemNewsComment = (Button) itemView.findViewById(R.id.btnItemNewsComment);
        this.btnItemNewsEdit = (Button)itemView.findViewById(R.id.btnItemNewsEdit);
    }


    public void bindToPlace(PlaceModel model, final OnButtonClickListenser onButtonClickListenser) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        this.tvItemNewsUserId.setText(model.getUser().getDisplayName());
        this.tvItemNewsCreated.setText((new Date(model.getCreated())).toString());
        this.tvItemNewsLike.setText(Integer.toString(model.getCountLike()));
        this.tvItemNewsComment.setText(Integer.toString(model.getCountComment()));
        this.tvItemNewsLocation.setText(model.getLocation().getName());
        this.tvItemNewsTitle.setText(model.getTitle());
        Glide.with(itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(storageRef.child(model.getImages().get(model.getImages().size() - 1)))
                .into(imgItemPlace);
        Glide.with(itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(storageRef.child(model.getUser().getPhotoUrl()))
                .into(imgItemUserImage);

        this.btnItemNewsLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListenser.ButtonLike_OnClick(view);
            }
        });
        this.btnItemNewsComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListenser.ButtonComment_OnClick(view);
            }
        });
        this.btnItemNewsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListenser.ButtonEdit_OnClick(view);
            }
        });
    }
}
