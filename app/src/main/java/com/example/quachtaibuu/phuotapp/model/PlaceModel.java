package com.example.quachtaibuu.phuotapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public class PlaceModel {

    private String title;
    private long created;

    private int countLike;
    private int countComment;

    private double latitude;
    private double longitude;

    private LocationModel location;
    private UserModel user;

    private String address;
    private String description;

    private Map<String, Boolean> likes = new HashMap<>();
    private List<String> images = new ArrayList<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PlaceModel{" +
                "title='" + title + '\'' +
                ", created=" + created +
                ", countLike=" + countLike +
                ", countComment=" + countComment +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location=" + location +
                ", user=" + user +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", images=" + images +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", this.title);
        result.put("address", this.address);
        result.put("description", this.description);
        result.put("created", new Date().getTime());
        result.put("latitude", this.latitude);
        result.put("longitude", this.longitude);
        result.put("location", location.toMap());
        result.put("images", this.images);
        result.put("user", this.user);
        return  result;
    }

//    public List<String> getImagesAsList() {
//        List<String> result = new ArrayList<>();
//        for(Map.Entry<String, String> entry: this.getImages().entrySet()) {
//            result.add(entry.getValue());
//        }
//        return  result;
//    }
}
