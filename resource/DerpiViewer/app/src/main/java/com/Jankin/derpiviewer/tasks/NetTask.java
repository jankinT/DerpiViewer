package com.Jankin.derpiviewer.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.Jankin.derpiviewer.tools.HttpsTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class NetTask extends AsyncTask<Void, Void, String> {
    private final String TAG = "NetTask";
    private String url;
    private String method = null;
    private TaskAction<JSONObject> taskAction;

    public NetTask(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTaskAction(TaskAction<JSONObject> taskAction) {
        this.taskAction = taskAction;
    }


    @Override
    protected String doInBackground(Void... voids) {
        String strJson;
        if(method == null) {
            if(url.startsWith("https")) {
                strJson = new HttpsTool(url).request();
            } else {
                strJson = new HttpsTool(url).httpRequest();
            }
        } else {
            if(url.startsWith("https")) {
                strJson = new HttpsTool(url).request(method);
            } else {
                strJson = new HttpsTool(url).httpRequest(method);
            }
        }


        return strJson;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if(s != null && !s.equals("error")) {
                JSONObject object = new JSONObject(s);
                taskAction.onAction(object);
            } else if(s != null) {
                taskAction.onAction(new JSONObject("{\"error\":\"error\"}"));
            }
            else {
                taskAction.onAction(null);
            }
        } catch (JSONException e) {
            taskAction.onAction(null);
            e.printStackTrace();
        }
    }
}
