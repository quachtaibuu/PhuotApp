package com.example.quachtaibuu.phuotapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;

import com.example.quachtaibuu.phuotapp.R;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public abstract class AbsRuntimePermission extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public abstract void onPermissionGranted(int code);

    public void requestPermission(final String[]permissions, final int requestCode) {

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;

        for (String permission: permissions) {
            permissionCheck = requestCode + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if(showRequestPermission) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.msgRequestPermission), Snackbar.LENGTH_INDEFINITE).setAction(R.string.msgRequestGrant, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(AbsRuntimePermission.this, permissions, requestCode);
                    }
                }).show();
            }else {
                ActivityCompat.requestPermissions(AbsRuntimePermission.this, permissions, requestCode);
            }
        }else {
            onPermissionGranted(requestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int permission: grantResults) {
            permissionCheck = permissionCheck + permission;
        }

        if(grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == permissionCheck) {
            onPermissionGranted(requestCode);
        }else {
            Snackbar.make(findViewById(android.R.id.content),  getString(R.string.msgRequestPermission), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.msgRequestGrant), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
            }).show();
        }
    }
}
