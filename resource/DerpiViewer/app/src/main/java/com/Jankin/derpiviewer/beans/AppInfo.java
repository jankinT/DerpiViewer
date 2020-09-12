package com.Jankin.derpiviewer.beans;

import com.Jankin.derpiviewer.tools.JsonTool;

import org.json.JSONException;
import org.json.JSONObject;

public class AppInfo {
    private String versionName;
    private String description;
    private String downloadUrl;
    public static final String VERSION_NAME = "versionName", DESCRIPTION = "description",
            DOWNLOAD_URL = "downloadUrl";

    public static final String[] NAMES = {VERSION_NAME, DESCRIPTION, DESCRIPTION};

    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public static AppInfo parse(JSONObject json) throws JSONException {
        AppInfo appInfo = new AppInfo();
        appInfo.versionName = JsonTool.jsonGetString(json, VERSION_NAME);
        appInfo.description = JsonTool.jsonGetString(json, DESCRIPTION);
        appInfo.downloadUrl = JsonTool.jsonGetString(json, DOWNLOAD_URL);

        return appInfo;
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject json = new JSONObject();
        JsonTool.jsonPutString(json, VERSION_NAME, versionName);
        JsonTool.jsonPutString(json, DESCRIPTION, description);
        JsonTool.jsonPutString(json, DOWNLOAD_URL, downloadUrl);

        return json;
    }
}
