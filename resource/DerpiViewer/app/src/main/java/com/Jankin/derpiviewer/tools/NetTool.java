package com.Jankin.derpiviewer.tools;

import com.Jankin.derpiviewer.beans.AppInfo;
import com.Jankin.derpiviewer.beans.DPComment;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.Str;
import com.Jankin.derpiviewer.tasks.NetTask;
import com.Jankin.derpiviewer.tasks.TaskAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class NetTool {
    public static void getFeaturedPicture(Action<DPImage> action, Action<Void> error) {
        String url = Str.URL_HEAD + Str.FEATURED;
        NetTask task = new NetTask(url);
        task.setTaskAction(obj -> {
            try {
                if(obj != null && !obj.isNull("image")) {
                    action.act(DPImage.getDPImageByJSON(obj.getJSONObject("image")));
                } else if(obj != null) {
                    error.act(null);
                }
                else {
                    action.act(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        task.execute();
    }

    public static void getTrendingImages(int page, Action<ArrayList<DPImage>> action) {
        String url = Str.URL_HEAD + Str.TRENDING + "&page=" + page + AppData.filter;
        NetTask task = new NetTask(url);
        task.setTaskAction(new TaskAction<JSONObject>() {
            @Override
            public void onAction(JSONObject obj) {
                if(obj != null) {
                    try {
                        ArrayList<DPImage> images = DPImage.getDPImageListByJSONArray(
                                obj.getJSONArray("images"));
                        action.act(images);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    action.act(null);
                }
            }
        });
        task.execute();
    }

    public static void getSearchedImages(String q, int page, String sf, String sd, Action<ArrayList<DPImage>> action) {
        q = URLEncoder.encode(q);
        String url = Str.URL_HEAD + Str.SEARCH + q + "&page=" + page + "&sf=" + sf + "&sd=" + sd +
                AppData.filter;
        NetTask task = new NetTask(url);
        task.setTaskAction(new TaskAction<JSONObject>() {
            @Override
            public void onAction(JSONObject obj) {
                if(obj != null) {
                    try {
                        ArrayList<DPImage> images = DPImage.getDPImageListByJSONArray(
                                obj.getJSONArray("images"));
                        action.act(images);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        action.act(null);
                    }
                } else {
                    action.act(null);
                }
            }
        });
        task.execute();
    }

    public static void getComments(int id, int page, Action<ArrayList<DPComment>> action) {
        String url = Str.URL_HEAD + "search/comments?q=image_id:" + id + "&page="
                + page + AppData.filter;

        NetTask task = new NetTask(url);
        task.setTaskAction(new TaskAction<JSONObject>() {
            @Override
            public void onAction(JSONObject obj) {
                if(obj != null) {
                    ArrayList<DPComment> comments;
                    JSONArray array;
                    try {
                        array = obj.getJSONArray("comments");
                        comments = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            comments.add(new DPComment(array.getJSONObject(i)));
                        }
                        action.act(comments);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        action.act(null);
                    }
                } else {
                    action.act(null);
                }
            }
        });
        task.execute();
    }

    public static void checkUpdate(Action<AppInfo> action) {
        final String URL = "http://39.105.48.87:8080/Derpi/api/app/version";
        NetTask task = new NetTask(URL);
        task.setTaskAction(obj -> {
            if(obj != null) {
                try {
                    AppInfo appInfo = AppInfo.parse(obj);
                    action.act(appInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    action.act(null);
                }
            } else {
                action.act(null);
            }
        });
        task.execute();
    }

    public interface Action<T> {
        void act(T t);
    }
}
