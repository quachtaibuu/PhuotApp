package com.example.quachtaibuu.phuotapp.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quach Tai Buu on 2017-08-07.
 */

public class UserModel {
    private String uid;
    private String displayName;
    private String email;
    private Uri photoUrl;
    private List<PlaceModel> places = new ArrayList<>();

    public UserModel() {
    }

    public UserModel(String uid, String displayName, String email, Uri photoUrl) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public UserModel(String uid, String displayName, String email, Uri photoUrl, List<PlaceModel> places) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.places = places;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<PlaceModel> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceModel> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl=" + photoUrl +
                ", place=" + places.toString() +
                '}';
    }
}
