package com.example.quachtaibuu.phuotapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.INotificationSideChannel;

import com.example.quachtaibuu.phuotapp.LoginActivity;
import com.example.quachtaibuu.phuotapp.model.UserModel;

/**
 * Created by Quach Tai Buu on 2017-08-12.
 */

public class UserSessionManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private final int PRIVATE_MODE = 0;
    private final String PREF_NAME = "UserInfo";

    private final String NAME_IS_LOGGED= "isLogged";
    private final String NAME_DISPLAY_NAME = "displayName";
    private final String NAME_EMAIL = "email";
    private final String NAME_PHOTO_URL = "photoUrl";
    private final String NAME_USERNAME = "username";
    private final String NAME_IS_ADMIN = "isAdmin";
    private final String NAME_USER_KEY = "userKey";

    public UserSessionManager(Context context) {
        this.mContext = context;
        this.mSharedPreferences = this.mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.mEditor = this.mSharedPreferences.edit();
    }

    public void createUserInfo(UserModel model) {

        this.mEditor.putBoolean(NAME_IS_LOGGED, true);
        this.mEditor.putString(NAME_DISPLAY_NAME, model.getDisplayName());
        this.mEditor.putString(NAME_EMAIL, model.getEmail());
        this.mEditor.putString(NAME_PHOTO_URL, model.getPhotoUrl());
        this.mEditor.putString(NAME_USERNAME, model.getUsername());
        this.mEditor.putBoolean(NAME_IS_ADMIN, model.isAdmin());
        this.mEditor.putString(NAME_USER_KEY, model.getUserKey());

        this.mEditor.commit();
    }

    public UserModel getUserDetails() {

        UserModel model = new UserModel();

        model.setPhotoUrl(this.mSharedPreferences.getString(NAME_PHOTO_URL, null));
        model.setAdmin(this.mSharedPreferences.getBoolean(NAME_IS_ADMIN, false));
        model.setDisplayName(this.mSharedPreferences.getString(NAME_DISPLAY_NAME, null));
        model.setEmail(this.mSharedPreferences.getString(NAME_EMAIL, null));
        model.setUsername(this.mSharedPreferences.getString(NAME_USERNAME, null));
        model.setUserKey(this.mSharedPreferences.getString(NAME_USER_KEY, null));

        return model;

    }

    public boolean isLogged() {
        return this.mSharedPreferences.getBoolean(NAME_IS_LOGGED, false);
    }

    public void logout() {
        this.mEditor.clear();
        this.mEditor.commit();

        Intent intent = new Intent(mContext, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }

}
