package com.example.quachtaibuu.phuotapp.holder;

import android.content.ContentResolver;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.quachtaibuu.phuotapp.R;

/**
 * Created by Quach Tai Buu on 2017-08-02.
 */

public class BitmapRecyclerViewHolder extends RecyclerView.ViewHolder {

    public CardView btnItemBitmapRemovePicture;
    public ImageView imgItemBitmapPicture;
    public ContentResolver contentResolver;

    public BitmapRecyclerViewHolder(View itemView) {
        super(itemView);

        this.btnItemBitmapRemovePicture = (CardView) itemView.findViewById(R.id.btnItemBitMapRemovePicture);
        this.imgItemBitmapPicture = (ImageView)itemView.findViewById(R.id.imgItemBitMapPicture);
        this.contentResolver = itemView.getContext().getContentResolver();
    }
}
