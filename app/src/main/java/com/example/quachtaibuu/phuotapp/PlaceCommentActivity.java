package com.example.quachtaibuu.phuotapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quachtaibuu.phuotapp.holder.PlaceCommentViewHolder;
import com.example.quachtaibuu.phuotapp.holder.PlaceRecyclerViewHolder;
import com.example.quachtaibuu.phuotapp.model.PlaceCommentModel;
import com.example.quachtaibuu.phuotapp.model.PlaceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaceCommentActivity extends BaseActivity {

    public static final String EXTRA_PLACE_KEY = "place_key";
    private String mPlaceKey;
    private Toolbar mToolbar;

    private DatabaseReference mDatabase;
    private Query mQuery;
    private FirebaseRecyclerAdapter mAdapter;

    private EditText edPlaceCommentBody;
    private Button btnPlaceCommentPost;
    private RecyclerView rcvPlaceCommentPost;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_comment);

        this.mPlaceKey = getIntent().getStringExtra(EXTRA_PLACE_KEY);

        if (this.mPlaceKey == null) {
            throw new IllegalArgumentException("Place key not found!");
        }

        this.mToolbar = (Toolbar) findViewById(R.id.toolBarPlaceComment);
        setSupportActionBar(this.mToolbar);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        getWindow().setLayout((int) (dm.widthPixels * 0.9), (int) (dm.heightPixels * 0.9));

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mQuery = this.mDatabase.child("place-comments").child(this.mPlaceKey).orderByKey();
        this.edPlaceCommentBody = (EditText) findViewById(R.id.edPlaceCommentBody);
        this.btnPlaceCommentPost = (Button) findViewById(R.id.btnPlaceCommentPost);
        this.rcvPlaceCommentPost = (RecyclerView) findViewById(R.id.rcvPlaceCommentPost);


        this.mAdapter = new FirebaseRecyclerAdapter<PlaceCommentModel, PlaceCommentViewHolder>(PlaceCommentModel.class, R.layout.item_place_comment, PlaceCommentViewHolder.class, this.mQuery) {
            @Override
            protected void populateViewHolder(PlaceCommentViewHolder viewHolder, PlaceCommentModel model, int position) {
                DatabaseReference ref = getRef(position);

                viewHolder.bindToPlaceComment(model);

            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() - (position + 1));
            }
        };

        this.mLayoutManager = new LinearLayoutManager(this);
        this.rcvPlaceCommentPost.setLayoutManager(this.mLayoutManager);
        this.rcvPlaceCommentPost.setHasFixedSize(true);
        this.rcvPlaceCommentPost.setAdapter(this.mAdapter);

        this.btnPlaceCommentPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceCommentModel comment = new PlaceCommentModel();
                comment.setText(edPlaceCommentBody.getText().toString());
                comment.setUser(getCurrentUser());

                DatabaseReference reference = mDatabase.child("place-comments").child(mPlaceKey);
                String key = Long.toString(comment.getCreated()); //reference.push().getKey();

//                Map<String, Object> childUpdates = new HashMap<>();
//                childUpdates.put("/places-comments/" + key, comment.toMap());
//                childUpdates.put("/places/comments/" + key, comment.toMap());

                reference.child(key).updateChildren(comment.toMap())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                edPlaceCommentBody.setText(null);
                                edPlaceCommentBody.clearFocus();
                                Toast.makeText(PlaceCommentActivity.this, getString(R.string.msgSuccessInsertedForComment), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PlaceCommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                updateCountComment(mDatabase.child("places").child(mPlaceKey));
                updateCountComment(mDatabase.child("user-places").child(getUserId()).child(mPlaceKey));
            }
        });

    }

    private void updateCountComment(final DatabaseReference ref) {

        mDatabase.child("place-comments").child(mPlaceKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Long count = dataSnapshot.getChildrenCount();

                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        final PlaceModel place = mutableData.getValue(PlaceModel.class);

                        if(place == null) {
                            return Transaction.success(mutableData);
                        }

                        place.setCountComment(count.intValue());
                        mutableData.setValue(place);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PlaceCommentActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
