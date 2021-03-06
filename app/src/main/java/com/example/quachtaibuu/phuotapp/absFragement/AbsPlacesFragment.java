package com.example.quachtaibuu.phuotapp.absFragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.AddNewPlaceActivity;
import com.example.quachtaibuu.phuotapp.BaseActivity;
import com.example.quachtaibuu.phuotapp.PlaceCommentActivity;
import com.example.quachtaibuu.phuotapp.PlaceDetailActivity;
import com.example.quachtaibuu.phuotapp.R;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.example.quachtaibuu.phuotapp.model.UserModel;
import com.example.quachtaibuu.phuotapp.utils.PhuotAppDatabase;
import com.example.quachtaibuu.phuotapp.utils.UserSessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public abstract class AbsPlacesFragment extends Fragment {

    private RecyclerView rcvNewsFeeds;
    private List<PlaceModel> placeModels = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mDatabase;
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isFirstTime = true;

    private String mPlaceKey;
    private UserSessionManager mSession;
    private UserModel mSessionUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_news_feed, container, false);

        //this.loadData();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        Query postQuery = this.getQuery(this.mDatabase); //this.mDatabase.child("places").orderByChild("created").limitToFirst(100);
        this.mSession = new UserSessionManager(getContext().getApplicationContext());
        this.mSessionUser = this.mSession.getUserDetails();

        this.mDatabase.child(PhuotAppDatabase.USERS).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.mAdapter = new FirebaseRecyclerAdapter<PlaceModel, PlaceRecyclerViewHolder>(PlaceModel.class, R.layout.item_news, PlaceRecyclerViewHolder.class, postQuery) {

            @Override
            protected void populateViewHolder(final PlaceRecyclerViewHolder viewHolder, final PlaceModel model, int position) {
                final DatabaseReference ref = getRef(position);

                if (model.getLikes().containsKey(mUser.getUid())) {
                    viewHolder.btnItemNewsLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_black_24dp, 0, 0, 0);
                    if (!mSessionUser.isAdmin()) {
                        viewHolder.btnItemNewsEdit.setVisibility(View.INVISIBLE);
                    }
                } else {
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
                    public void ButtonLike_OnClick(View view) {
                        //DatabaseReference userLikeRef = mDatabase.child(PhuotAppDatabase.USER_LIKES).child(mUser.getUid()).child(ref.getKey());
                        DatabaseReference placesRef = mDatabase.child(PhuotAppDatabase.PLACES).child(ref.getKey());
                        DatabaseReference userPlacesRef = mDatabase.child(PhuotAppDatabase.USER_PLACES).child(mUser.getUid()).child(ref.getKey());
                        DatabaseReference locationPlacesRef = mDatabase.child(PhuotAppDatabase.LOCATION_PLACES).child(model.getLocation().getLocationKey()).child(ref.getKey());

                        //onLikeClick(userLikeRef);
                        onLikeClick(placesRef);
                        onLikeClick(userPlacesRef);
                        onLikeClick(locationPlacesRef);
                    }

                    @Override
                    public void ButtonComment_OnClick(View view) {
                        Intent intent = new Intent(getContext(), PlaceCommentActivity.class);
                        intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_KEY, ref.getKey());
                        startActivity(intent);
                    }

                    @Override
                    public void ButtonEdit_OnClick(View view) {
                        mPlaceKey = ref.getKey();
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                        MenuInflater menuInflater = popupMenu.getMenuInflater();
                        menuInflater.inflate(R.menu.menu_post, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_post_delete:
                                        onActionDeleteClick(mPlaceKey, model);
                                        break;
                                    case R.id.action_post_edit:
                                        onActionEditClick(mPlaceKey, model);
                                        break;
                                }
                                return true;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }

        };


        this.layoutManager = new LinearLayoutManager(rootView.getContext());
        this.rcvNewsFeeds = (RecyclerView) rootView.findViewById(R.id.rcvNewsFeeds);
        this.rcvNewsFeeds.setHasFixedSize(true);
        this.rcvNewsFeeds.setLayoutManager(this.layoutManager);
        this.rcvNewsFeeds.setAdapter(this.mAdapter);

        return rootView;
    }

    protected void onActionEditClick(String key, final PlaceModel place) {
        Intent intent = new Intent(getContext(), AddNewPlaceActivity.class);
        intent.putExtra("place", new Gson().toJson(place));
        intent.putExtra("placeKey", key);
        startActivity(intent);
    }

    protected void onActionDeleteClick(final String key, final PlaceModel place) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        builder.setTitle(getString(R.string.msgTitleDeletePlace));
        builder.setMessage(getString(R.string.msgConfirmDelete));
        builder.setPositiveButton(getString(R.string.msgPositiveButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Task<?>[] tasks = new Task[]{
                        mDatabase.child(PhuotAppDatabase.PLACES).child(key).removeValue(),
                        mDatabase.child(PhuotAppDatabase.USER_PLACES).child(mUser.getUid()).child(key).removeValue(),
                        mDatabase.child(PhuotAppDatabase.LOCATION_PLACES).child(place.getLocation().getLocationKey()).child(key).removeValue()
                };

                Tasks.whenAll(tasks).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton(getString(R.string.msgNegativeButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void onLikeClick(DatabaseReference ref) {

        ref.runTransaction(new Transaction.Handler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                PlaceModel place = mutableData.getValue(PlaceModel.class);
                if (place == null) {
                    return Transaction.success(mutableData);
                }

                if (place.getLikes().containsKey(mUser.getUid())) {
                    place.setCountLike(place.getCountLike() - 1);
                    place.getLikes().remove(mUser.getUid());
                } else {
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
        if (this.mAdapter != null) {
            this.mAdapter.cleanup();
        }
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
