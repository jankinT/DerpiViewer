package com.Jankin.derpiviewer.beans;

import org.json.JSONException;
import org.json.JSONObject;

public class DPComment {
    private String author;
    private String avatar;
    private String createdAt;
    private Integer imageId;
    private Integer userId;
    private String body;

    public DPComment(JSONObject obj) throws JSONException {
        this.author = obj.getString("author");
        this.avatar = obj.getString("avatar");
        this.createdAt = obj.getString("created_at");
        this.imageId = obj.getInt("image_id");
        this.body = obj.getString("body");
        if(!obj.isNull("user_id")) {
            this.userId = obj.getInt("user_id");
        } else {
            userId = null;
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getImageId() {
        return imageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }
}
