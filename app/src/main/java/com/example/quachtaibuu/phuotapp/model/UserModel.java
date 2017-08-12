package com.example.quachtaibuu.phuotapp.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Quach Tai Buu on 2017-08-07.
 */

public class UserModel {

    @JsonIgnore
    private String userKey;
    private String displayName;
    private String email;
    private String username;
    //private Uri photoUrl;
    private String photoUrl;
    private boolean isAdmin;

    public UserModel() {
    }

    public UserModel(String displayName, String email, String username, String photoUrl) {
        this.displayName = displayName;
        this.email = email;
        this.username = username;
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    //    @Override
//    public String toString() {
//        return "UserModel{" +
//                "displayName='" + displayName + '\'' +
//                ", email='" + email + '\'' +
//                ", username='" + username + '\'' +
//                ", photoUrl=" + photoUrl +
//                '}';
//    }
//
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("displayName", this.displayName);
//        result.put("email", this.email);
//        result.put("username", this.username);
//        result.put("photoUrl", this.photoUrl);
//        return result;
//    }
}
