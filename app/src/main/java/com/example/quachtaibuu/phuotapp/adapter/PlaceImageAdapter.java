package com.example.quachtaibuu.phuotapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quachtaibuu.phuotapp.R;

import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-04.
 */

public class PlaceImageAdapter extends PagerAdapter {

    private List<Integer> lstData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PlaceImageAdapter(Context context, List<Integer> lstData) {
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
        ImageView imgSlidePlaceImage = slidePlaceView.findViewById(R.id.imgSlidePlaceImage);
        imgSlidePlaceImage.setImageResource(this.lstData.get(position));
        container.addView(slidePlaceView, 0);
        return slidePlaceView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
