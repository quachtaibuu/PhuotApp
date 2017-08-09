package com.example.quachtaibuu.phuotapp.model;

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
    private int userImage;
    private String username;
    private long created;
    private String locationName;
    private int placeImage;

    private int countLike;
    private int countComment;
    private int getCountShare;

    private double latitude;
    private double longitude;

    private LocationModel location;
    private UserModel user;

    private String address;
    private String description;

    private Map<String, Boolean> likes = new HashMap<>();
    private Map<String, String> images = new HashMap<>();

    public PlaceModel() {
    }

    public PlaceModel(String title, int userImage, String username, long created, String locationName, int placeImage, int countLike, int countComment, int getCountShare) {
        this.title = title;
        this.userImage = userImage;
        this.username = username;
        this.created = created;
        this.locationName = locationName;
        this.placeImage = placeImage;
        this.countLike = countLike;
        this.countComment = countComment;
        this.getCountShare = getCountShare;
    }

    public PlaceModel(String title, int userImage, String username, long created, String locationName, int placeImage, int countLike, int countComment, int getCountShare, double latitude, double longitude) {
        this.title = title;
        this.userImage = userImage;
        this.username = username;
        this.created = created;
        this.locationName = locationName;
        this.placeImage = placeImage;
        this.countLike = countLike;
        this.countComment = countComment;
        this.getCountShare = getCountShare;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PlaceModel(String title, int userImage, String username, long created, String locationName, int placeImage, int countLike, int countComment, int getCountShare, double latitude, double longitude, LocationModel location, UserModel user) {
        this.title = title;
        this.userImage = userImage;
        this.username = username;
        this.created = created;
        this.locationName = locationName;
        this.placeImage = placeImage;
        this.countLike = countLike;
        this.countComment = countComment;
        this.getCountShare = getCountShare;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(int placeImage) {
        this.placeImage = placeImage;
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

    public int getGetCountShare() {
        return getCountShare;
    }

    public void setGetCountShare(int getCountShare) {
        this.getCountShare = getCountShare;
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


    @Override
    public String toString() {
        return "PlaceModel{" +
                "title='" + title + '\'' +
                ", userImage=" + userImage +
                ", username='" + username + '\'' +
                ", created='" + created + '\'' +
                ", locationName='" + locationName + '\'' +
                ", placeImage=" + placeImage +
                ", countLike=" + countLike +
                ", countComment=" + countComment +
                ", getCountShare=" + getCountShare +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                //", location=" + location.toString() +
                //", user=" + user.toString() +
                '}';
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
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

    public List<String> getImagesAsList() {
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, String> entry: this.getImages().entrySet()) {
            result.add(entry.getValue());
        }
        return  result;
    }
}
