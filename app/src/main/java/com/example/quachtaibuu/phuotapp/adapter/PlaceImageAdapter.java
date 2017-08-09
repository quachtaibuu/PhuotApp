package com.example.quachtaibuu.phuotapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.quachtaibuu.phuotapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-04.
 */

public class PlaceImageAdapter extends PagerAdapter {

    private List<String> lstData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PlaceImageAdapter(Context context, List<String> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View slidePlaceView = inflater.inflate(R.layout.fragment_slide_place_image, container, false);
        ImageView imgSlidePlaceImage = (ImageView) slidePlaceView.findViewById(R.id.imgSlidePlaceImage);
        //imgSlidePlaceImage.setImageResource(this.lstData.get(position));
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference().child(this.lstData.get(position)))
                .into(imgSlidePlaceImage);
        container.addView(slidePlaceView, 0);
        return slidePlaceView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
