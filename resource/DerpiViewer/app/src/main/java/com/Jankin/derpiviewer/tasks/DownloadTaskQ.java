package com.Jankin.derpiviewer.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import java.io.OutputStream;
import java.util.Objects;

public class DownloadTaskQ extends AsyncTask<DPImage, Void, Boolean> {
    private final String TAG = "DownloadTask";
    private Context context;

    public DownloadTaskQ(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected Boolean doInBackground(DPImage... dpImages) {
        DPImage image = dpImages[0];
        String url = image.getViewUrl();

        String fileName = "" + image.getId();
        Uri uri;
        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, image.getMimeType());
        uri = resolver.insert(external, values);
        Log.d(TAG, "doInBackground: " + uri.getPath());
        HttpsTool httpsTool = new HttpsTool(url);
        InputStream in = httpsTool.getStream();
        try {
            OutputStream out = resolver.openOutputStream(uri);
            assert out != null;
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            out.flush();
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Toast.makeText(context, R.string.str_download_done,Toast.LENGTH_SHORT).show();
    }
}
