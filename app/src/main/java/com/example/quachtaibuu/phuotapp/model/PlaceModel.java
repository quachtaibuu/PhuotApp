package com.example.quachtaibuu.phuotapp.model;

/**
 * Created by Quach Tai Buu on 2017-08-01.
 */

public class PlaceModel {

    private String title;
    private int userImage;
    private String username;
    private String created;
    private String locationName;
    private int placeImage;

    private int countLike;
    private int countComment;
    private int getCountShare;

    private double latitude;
    private double longitude;

    private LocationModel location;
    private UserModel user;

    public PlaceModel() {
    }

    public PlaceModel(String title, int userImage, String username, String created, String locationName, int placeImage, int countLike, int countComment, int getCountShare) {
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

    public PlaceModel(String title, int userImage, String username, String created, String locationName, int placeImage, int countLike, int countComment, int getCountShare, double latitude, double longitude) {
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

    public PlaceModel(String title, int userImage, String username, String created, String locationName, int placeImage, int countLike, int countComment, int getCountShare, double latitude, double longitude, LocationModel location, UserModel user) {
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
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
}
