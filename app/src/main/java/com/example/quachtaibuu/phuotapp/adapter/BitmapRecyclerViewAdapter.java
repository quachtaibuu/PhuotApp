package com.example.quachtaibuu.phuotapp.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.BitmapRecyclerViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-02.
 */

public class BitmapRecyclerViewAdapter extends RecyclerView.Adapter<BitmapRecyclerViewHolder> {

    public interface OnButtonClickListener {
        void onRemoveClick(int postion);
    }

    private List<Uri> lstData = new ArrayList<>();
    private OnButtonClickListener mOnClickListener;

    public BitmapRecyclerViewAdapter() {

    }

    public BitmapRecyclerViewAdapter(List<Uri> data) {
        this.lstData = data;
    }

    public BitmapRecyclerViewAdapter(List<Uri> data, OnButtonClickListener mOnClickListener) {
        this.lstData = data;
        this.mOnClickListener = mOnClickListener;
    }

    public void setListData(List<Uri> data) {
        this.lstData = data;
    }

    public void setOnButtonClickListener(OnButtonClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    public BitmapRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.item_bitmap, parent, false);
        return new BitmapRecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final BitmapRecyclerViewHolder holder, final int position) {
        final Uri imgUri = lstData.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(holder.contentResolver, imgUri);
            holder.imgItemBitmapPicture.setImageBitmap(bitmap);
            holder.btnItemBitmapRemovePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onRemoveClick(position);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }
}
