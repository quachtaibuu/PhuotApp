package com.example.quachtaibuu.phuotapp;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;

import com.example.quachtaibuu.phuotapp.utils.AbsRuntimePermission;

public class LoginActivity extends AbsRuntimePermission {

    private final String permissions[] = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    private final int APP_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.requestPermission(permissions, APP_REQUEST_CODE);
    }


    public void btnLoginFB_OnClick(View v) {
        //check login OK
        this.showMainActivity();
    }

    public void btnLoginGG_OnClick(View v) {
        //check login OK
        //this.showMainActivity();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPermissionGranted(int code) {

    }
}
