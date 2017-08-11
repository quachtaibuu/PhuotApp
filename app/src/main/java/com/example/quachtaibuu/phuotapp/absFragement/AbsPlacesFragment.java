package com.example.quachtaibuu.phuotapp.absFragement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quachtaibuu.phuotapp.PlaceCommentActivity;
import com.example.quachtaibuu.phuotapp.PlaceDetailActivity;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.List;


public abstract class AbsPlacesFragment extends Fragment {

    private RecyclerView rcvNewsFeeds;
    private List<PlaceModel> placeModels = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_news_feed, container, false);

        //this.loadData();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        Query postQuery = this.getQuery(this.mDatabase); //this.mDatabase.child("places").orderByChild("created").limitToFirst(100);

        this.mAdapter = new FirebaseRecyclerAdapter<PlaceModel, PlaceRecyclerViewHolder>(PlaceModel.class, R.layout.item_news, PlaceRecyclerViewHolder.class, postQuery) {

            @Override
            protected void populateViewHolder(final PlaceRecyclerViewHolder viewHolder, final PlaceModel model, int position) {
                final DatabaseReference ref = getRef(position);

                if(model.getLikes().containsKey(mUser.getUid())) {
                    viewHolder.btnItemNewsLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                }else {
                    viewHolder.btnItemNewsLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0, 0, 0);
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), PlaceDetailActivity.class);
                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, ref.getKey());
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPlace(model, new PlaceRecyclerViewHolder.OnButtonClickListenser() {
                    @Override
                    public void ButtonLike_OnClick() {
                        DatabaseReference userLikeRef = mDatabase.child("user-likes").child(mUser.getUid()).child(ref.getKey());
                        DatabaseReference placesRef = mDatabase.child("places").child(ref.getKey());
                        DatabaseReference userPlacesRef = mDatabase.child("user-places").child(mUser.getUid()).child(ref.getKey());
                        DatabaseReference locationPlacesRef = mDatabase.child("location-places").child(ref.getKey());

                        onLikeClick(userLikeRef);
                        onLikeClick(placesRef);
                        onLikeClick(userPlacesRef);
                        onLikeClick(locationPlacesRef);
                    }

                    @Override
                    public void ButtonComment_OnClick() {
                        Intent intent = new Intent(getContext(), PlaceCommentActivity.class);
                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, ref.getKey());
                        startActivity(intent);
                    }
                });

//                if(isFirstTime) {
//                    isFirstTime = false;
//                    rcvNewsFeeds.smoothScrollToPosition(getItemCount() - 1);
//                    layoutManager.scrollToPosition(getItemCount() - 1);
//                }

            }

//            @Override
//            public long getItemId(int position) {
//                return super.getItemId(getItemCount() - (position + 1));
//            }
        };


        this.layoutManager = new LinearLayoutManager(rootView.getContext());
        this.rcvNewsFeeds = (RecyclerView)rootView.findViewById(R.id.rcvNewsFeeds);
        this.rcvNewsFeeds.setHasFixedSize(true);
        this.rcvNewsFeeds.setLayoutManager(this.layoutManager);
        this.rcvNewsFeeds.setAdapter(this.mAdapter);

        return rootView;
    }

    private void onLikeClick(DatabaseReference ref) {

        ref.keepSynced(true);

        ref.runTransaction(new Transaction.Handler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                PlaceModel place = mutableData.getValue(PlaceModel.class);
                if(place == null) {
                    return Transaction.success(mutableData);
                }

                if(place.getLikes().containsKey(mUser.getUid())) {
                    place.setCountLike(place.getCountLike() - 1);
                    place.getLikes().remove(mUser.getUid());
                }else {
                    place.setCountLike(place.getCountLike() + 1);
                    place.getLikes().put(mUser.getUid(), true);
                }

                mutableData.setValue(place);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(this.mAdapter != null) {
            this.mAdapter.cleanup();
        }
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
