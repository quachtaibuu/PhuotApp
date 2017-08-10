package com.example.quachtaibuu.phuotapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Quach Tai Buu on 2017-08-11.
 */

public class ItemPlaceSmViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgItemPlaceSmPicture;
    private TextView tvItemPlaceSmTitle;
    private TextView tvItemPlaceSmAddress;
    private TextView tvItemPlaceSmLike;

    private FirebaseStorage mStorage;

    public ItemPlaceSmViewHolder(View itemView) {
        super(itemView);

        this.imgItemPlaceSmPicture = (ImageView)itemView.findViewById(R.id.imgItemPlaceSmPicture);
        this.tvItemPlaceSmAddress = (TextView)itemView.findViewById(R.id.tvItemPlaceSmAddress);
        this.tvItemPlaceSmTitle = (TextView)itemView.findViewById(R.id.tvItemPlaceSmTitle);
        this.tvItemPlaceSmLike = (TextView)itemView.findViewById(R.id.tvItemPlaceSmLike);

        this.mStorage = FirebaseStorage.getInstance();
    }


    public void bindToPlace(PlaceModel model) {

        Glide.with(itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(this.mStorage.getReference(model.getImages().get(model.getImages().size() - 1)))
                .override(80, 80)
                .into(this.imgItemPlaceSmPicture);

        this.tvItemPlaceSmTitle.setText(model.getTitle());
        this.tvItemPlaceSmAddress.setText(model.getAddress());
        this.tvItemPlaceSmLike.setText(Integer.toString(model.getCountLike()));

    }

}
