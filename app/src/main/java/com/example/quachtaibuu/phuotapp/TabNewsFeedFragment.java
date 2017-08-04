package com.example.quachtaibuu.phuotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.adapter.PlaceRecyclerViewAdapter;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.example.quachtaibuu.phuotapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class TabNewsFeedFragment extends Fragment {

    private RecyclerView rcvNewsFeeds;
    private List<PlaceModel> placeModels = new ArrayList<>();
    private PlaceRecyclerViewAdapter placeRecyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_news_feed, container, false);

        this.loadData();

        this.layoutManager = new LinearLayoutManager(rootView.getContext());
        this.placeRecyclerViewAdapter = new PlaceRecyclerViewAdapter(this.placeModels);

        this.rcvNewsFeeds = (RecyclerView)rootView.findViewById(R.id.rcvNewsFeeds);
        this.rcvNewsFeeds.setHasFixedSize(true);
        this.rcvNewsFeeds.setLayoutManager(this.layoutManager);
        this.rcvNewsFeeds.setAdapter(this.placeRecyclerViewAdapter);

        this.rcvNewsFeeds.addOnItemTouchListener(new RecyclerItemClickListener(rootView.getContext(), this.rcvNewsFeeds, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), placeModels.get(position).toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), PlaceDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        return rootView;
    }

    private void loadData() {
        placeModels.add(new PlaceModel("Miếu bà chúa xứ Núi Sam", R.drawable.news_avatar, "Góc Thư Giản", "1 tháng 8 lúc 03:11 SA", "An Giang", R.drawable.pc_mieuba, 6, 4, 2));
        placeModels.add(new PlaceModel("Núi Ông Két", R.drawable.news_avt1, "Quách Tài Bửu", "1 tháng 8 lúc 04:11 SA", "An Giang", R.drawable.pc_nuicam, 10, 5, 15));
    }
}
