package com.Jankin.derpiviewer.tools;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsTool {
    private static final String TAG = "HttpsTool";
    private String url;
    private String result;

    public HttpsTool(String url) {
        this.url = url;
    }

    public String request(String method) {
        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            result = getString(connection);

        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
            Log.e(TAG, "request: " + e.getMessage());
        }

        return result;
    }

    public String httpRequest(String method) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            result = getString(connection);

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            Log.e(TAG, "request: " + e.getMessage());
        }

        return result;
    }

    public String request() {
        return request("GET");
    }

    public String httpRequest() {
        return httpRequest("GET");
    }

    public InputStream getStream() {
        HttpsURLConnection connection;
        InputStream inputStream;
        try {
            connection = (HttpsURLConnection) new URL(url).openConnection();
            inputStream = connection.getInputStream();
            return inputStream;
        } catch (Exception e) {
            Log.e(TAG, "request: " + e.getMessage());
        }
        return null;
    }

    public boolean writeFile(InputStream in, FileOutputStream out) {
        byte[] bytes = new byte[1024];
        int len;
        try {
            while ((len = in.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            out.flush();
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getString(HttpsURLConnection connection) throws IOException {
        int length;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = connection.getInputStream();
        while ((length = input.read(bytes)) > -1) {
            out.write(bytes, 0, length);
        }
        String str = new String(out.toByteArray(), "utf-8");
        input.close();
        out.close();
        connection.disconnect();

        return str;
    }

    private String getString(HttpURLConnection connection) throws IOException {
        int length;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = connection.getInputStream();
        while ((length = input.read(bytes)) > -1) {
            out.write(bytes, 0, length);
        }
        String str = new String(out.toByteArray(), "utf-8");
        input.close();
        out.close();
        connection.disconnect();

        return str;
    }
}
