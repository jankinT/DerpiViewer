package com.Jankin.derpiviewer.tasks;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.UniTool;
import com.Jankin.derpiviewer.tools.HttpsTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class DownloadTask extends AsyncTask<DPImage, Void, File> {
    private final String TAG = "DownloadTask";
    private Context context;

    public DownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected File doInBackground(DPImage... dpImages) {
        DPImage image = dpImages[0];
        String url = image.getViewUrl();

        File dir = new File(Objects.requireNonNull(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS)).getPath());
        Log.d(TAG, "doInBackground: " + dir.getPath());
        String fileName = "" + image.getId() + "." + image.getFormat();
        File file = new File(dir, fileName);
        Log.d(TAG, "doInBackground: " + file.getPath());
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        HttpsTool httpsTool = new HttpsTool(url);
        InputStream stream = httpsTool.getStream();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            boolean flag = httpsTool.writeFile(stream, outputStream);
            if(flag) {
                return file;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(file != null) {
            Toast.makeText(context, R.string.str_download_done,Toast.LENGTH_SHORT).show();
            UniTool.scanFile(context, file);
        } else {
            Toast.makeText(context, R.string.str_download_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
