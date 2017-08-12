package com.example.quachtaibuu.phuotapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.quachtaibuu.phuotapp.model.UserModel;
import com.example.quachtaibuu.phuotapp.utils.UserSessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Quach Tai Buu on 2017-08-08.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private UserModel mUser;
    private UserSessionManager mSession;

    @Override
    protected void onStart() {
        super.onStart();
        this.mSession = new UserSessionManager(getApplicationContext());
        this.mUser = this.mSession.getUserDetails();
    }

    public void showProgressDialog() {
        if(this.mProgressDialog == null) {
            this.mProgressDialog = new ProgressDialog(this);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setMessage(getString(R.string.msgLoading));
        }
        this.mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if(this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }


    public UserModel getCurrentUser() {
        return this.mUser;
    }

    public String getUserId() {
        return this.mUser.getUserKey();
    }


}
