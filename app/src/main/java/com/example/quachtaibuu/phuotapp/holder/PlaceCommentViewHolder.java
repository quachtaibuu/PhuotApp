package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.model.PlaceCommentModel;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;

/**
 * Created by Quach Tai Buu on 2017-08-10.
 */

public class PlaceCommentViewHolder extends RecyclerView.ViewHolder {

    private TextView tvItemPlaceCommentUsername;
    private TextView tvItemPlaceCommentBody;
    private TextView tvItemPlaceCommentCreated;
    private ImageView imgItemPlaceCommentAvatar;

    public PlaceCommentViewHolder(View itemView) {
        super(itemView);

        this.tvItemPlaceCommentBody = (TextView)itemView.findViewById(R.id.tvItemPlaceCommentBody);
        this.tvItemPlaceCommentCreated = (TextView)itemView.findViewById(R.id.tvItemPlaceCommentCreated);
        this.tvItemPlaceCommentUsername = (TextView)itemView.findViewById(R.id.tvItemPlaceCommentUsername);
        this.imgItemPlaceCommentAvatar = (ImageView)itemView.findViewById(R.id.imgItemPlaceCommentAvatar);
    }

    public void bindToPlaceComment(PlaceCommentModel model) {

        this.tvItemPlaceCommentCreated.setText(new Date(model.getCreated()).toString());
        this.tvItemPlaceCommentUsername.setText(model.getUser().getDisplayName());
        this.tvItemPlaceCommentBody.setText(model.getText());

        Glide.with(itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference(model.getUser().getPhotoUrl()))
                .into(this.imgItemPlaceCommentAvatar);

    }
}
