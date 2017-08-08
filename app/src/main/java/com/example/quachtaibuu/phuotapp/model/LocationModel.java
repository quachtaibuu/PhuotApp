package com.example.quachtaibuu.phuotapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Quach Tai Buu on 2017-07-31.
 */

public class LocationModel {

    private String id;
    private String name;
    private String description;
    private int image;
    private List<PlaceModel> places = new ArrayList<>();
    private double latitude;
    private double longtitude;
    private Map<String, String> images = new HashMap<>();

    public LocationModel() {
    }

    public LocationModel(String id, String name, String description, int image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public LocationModel(String id, String name, String description, int image, List<PlaceModel> places) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.places = places;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitlongtitudeude(double longtitude) {
        this.longtitude = longtitude;
    }

    public List<PlaceModel> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceModel> places) {
        this.places = places;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", places=" + places.toString() +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", this.name);
        result.put("description", this.description);
        result.put("latitude", this.latitude);
        result.put("longtitude", this.longtitude);
        result.put("images", this.images);

        return result;
    }
}
