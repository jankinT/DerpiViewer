package com.Jankin.derpiviewer.beans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DPImage {
    public static final String GIF = "image/gif";
    public static final String JPEG = "image/jpeg";
    public static final String PNG = "image/png";
    public static final String SVG_XML = "image/svg+xml";
    public static final String WEBM = "video/webm";
    public static final String FULL = "full", LARGE = "large", MEDIUM = "medium", SMALL = "small",
            TALL = "tall", THUMB = "thumb", THUMB_SMALL = "thumb_small", THUMB_TINY = "thumb_tiny";
    private static final String[] SIZES = {FULL, LARGE, MEDIUM, SMALL, TALL, THUMB, THUMB_SMALL,
            THUMB_TINY};
    private int commentCount;
    private String createdAt;
    private String description;
    private int downvotes;
    private int faves;
    private String format;
    private int height;
    private boolean hiddenFromUsers;
    private int id;
    private String mimeType;
    private String name;
    private Map<String, String> representations;
    private int score;
    private String sourceUrl;
    private boolean spoilered;
    private int tagCount;
    private int[] tagIds;
    private String[] tags;
    private boolean thumbnailsGenerated;
    private String uploader;
    private int uploaderId = -1;
    private int upvotes;
    private String viewUrl;
    private int width;

    private DPImage() {

    }

    public static DPImage getDPImageByJSON(JSONObject json) throws JSONException {
        DPImage dpImage = new DPImage();
        dpImage.commentCount = json.getInt("comment_count");
        dpImage.createdAt = json.getString("created_at");
        dpImage.description = json.getString("description");
        dpImage.downvotes = json.getInt("downvotes");
        dpImage.faves = json.getInt("faves");
        dpImage.format = json.getString("format");
        dpImage.height = json.getInt("height");
        dpImage.hiddenFromUsers = json.getBoolean("hidden_from_users");
        dpImage.id = json.getInt("id");
        dpImage.mimeType = json.getString("mime_type");
        dpImage.name = json.getString("name");
        dpImage.score = json.getInt("score");
        dpImage.spoilered = json.getBoolean("spoilered");
        dpImage.tagCount = json.getInt("tag_count");
        dpImage.thumbnailsGenerated = json.getBoolean("thumbnails_generated");
        dpImage.upvotes = json.getInt("upvotes");
        dpImage.viewUrl = json.getString("view_url");
        dpImage.width = json.getInt("width");

        if(!json.isNull("source_url")) {
            dpImage.sourceUrl = json.getString("source_url");
        } else {
            dpImage.sourceUrl = null;
        }

        if(!json.isNull("uploader")) {
            dpImage.uploader = json.getString("uploader");
            dpImage.uploaderId = json.getInt("uploader_id");
        } else {
            dpImage.uploader = null;
            dpImage.uploaderId = -1;
        }

        JSONArray array = json.getJSONArray("tags");
        int len = dpImage.getTagCount();
        dpImage.tags = new String[len];
        for(int i=0; i<len; i++){
            dpImage.tags[i] = array.getString(i);
        }

        array = json.getJSONArray("tag_ids");
        dpImage.tagIds = new int[len];
        for(int i=0; i<len; i++){
            dpImage.tagIds[i] = array.getInt(i);
        }

        JSONObject jo = json.getJSONObject("representations");

        if(dpImage.isThumbnailsGenerated()) {
            dpImage.representations = new HashMap<>();
            for(String k : SIZES) {
                dpImage.representations.put(k, jo.getString(k));
            }
        } else {
            dpImage.representations = null;
        }

        return dpImage;
    }

    public static ArrayList<DPImage> getDPImageListByJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<DPImage> images = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            images.add(DPImage.getDPImageByJSON(jsonArray.getJSONObject(i)));
        }

        return images;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getFaves() {
        return faves;
    }

    public String getFormat() {
        return format;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHiddenFromUsers() {
        return hiddenFromUsers;
    }

    public int getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getRepresentations() {
        return representations;
    }

    public int getScore() {
        return score;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public boolean isSpoilered() {
        return spoilered;
    }

    public int getTagCount() {
        return tagCount;
    }

    public int[] getTagIds() {
        return tagIds;
    }

    public String[] getTags() {
        return tags;
    }

    public boolean isThumbnailsGenerated() {
        return thumbnailsGenerated;
    }

    public String getUploader() {
        return uploader;
    }

    public int getUploaderId() {
        return uploaderId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public int getWidth() {
        return width;
    }
}
