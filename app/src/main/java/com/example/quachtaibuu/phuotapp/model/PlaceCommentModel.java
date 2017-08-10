package com.example.quachtaibuu.phuotapp.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quach Tai Buu on 2017-08-10.
 */

public class PlaceCommentModel {

    private String text;
    private long created;
    private UserModel user;

    public PlaceCommentModel() {
        this.created = new Date().getTime();
    }

    public PlaceCommentModel(String text, long created, UserModel user) {
        this.text = text;
        this.created = created;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PostCommentModel{" +
                "text='" + text + '\'' +
                ", created=" + created +
                ", user=" + user +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("text", this.text);
        result.put("user", this.user);
        result.put("created", this.created);
        return result;
    }
}
