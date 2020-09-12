package com.Jankin.derpiviewer.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.tools.NetTool;

import java.io.File;
import java.util.Locale;

public class UniTool {
    private static TransTool transTool;
    private static PermissionTool permissionTool;
    private static MediaScannerConnection mediaScannerConnection;
    private static TransToSearchTool transToSearchTool;

    public static void setTransToSearchTool(TransToSearchTool transToSearchTool) {
        UniTool.transToSearchTool = transToSearchTool;
    }

    public static void scanFile(Context context, File file) {
        MediaScannerConnection.MediaScannerConnectionClient client =
                new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                mediaScannerConnection.scanFile(file.getParent(), null);
                mediaScannerConnection.scanFile(file.getPath(), null);
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                if(mediaScannerConnection != null) {
                    mediaScannerConnection.disconnect();
                    Log.d("Scanner", "onMediaScannerConnected: scanned");
                }
            }
        };
        mediaScannerConnection = new MediaScannerConnection(context, client);
        mediaScannerConnection.connect();
    }

    public static void setTransTool(TransTool transTool) {
        UniTool.transTool = transTool;
    }

    public static void trans(Fragment fragment) {
        if(transTool != null) {
            transTool.trans(fragment);
        }
    }

    public static void setPermissionTool(PermissionTool permissionTool) {
        UniTool.permissionTool = permissionTool;
    }

    public static void changeLanguage(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        Locale locale;
        switch (AppData.language) {
            default: locale = Locale.ENGLISH;break;
            case Language.ZH: locale = Locale.SIMPLIFIED_CHINESE;break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        res.updateConfiguration(config, dm);
    }

    public static String getPageText(int page) {
        String pageText;
        switch (AppData.language) {
            default: pageText = "page " + page;break;
            case Language.ZH: pageText = "第 " + page + " 页";break;
        }

        return pageText;
    }

    public static void checkPermissions() {
        permissionTool.check();
    }

    public static boolean isPermitted(Context context) {
        int permission = ContextCompat.checkSelfPermission(context,
                "android.permission.WRITE_EXTERNAL_STORAGE");

        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static void doSearch(String q) {
        AppData.currentShowing = AppData.SEARCH;
        transToSearchTool.trans(q);
    }

    public static void checkUpdates(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(new ProgressBar(context));
        builder.setTitle(R.string.str_check_for_update);
        builder.setIcon(R.drawable.ic_update_blue);
        AlertDialog dialog = builder.create();
        dialog.show();
        NetTool.checkUpdate(appInfo -> {
            dialog.hide();
            if(appInfo != null) {
                if (appInfo.getVersionName().equals(context.getResources().
                        getString(R.string.app_version))) {
                    noUpdate(context);
                } else {
                    if(AppData.language == Language.ZH) {
                        update(context, appInfo.getDownloadUrl());
                    } else {
                        update(context, "https://github.com/jankinT/DerpiViewer/releases");
                    }
                }
            } else {
                Toast.makeText(context, R.string.str_loading_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void update(Context context, String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog;
        builder.setMessage(R.string.str_new_version_found);
        builder.setTitle(R.string.str_update);
        builder.setIcon(R.drawable.ic_update_blue);
        builder.setPositiveButton(R.string.str_update, (dialog1, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        });
        builder.setNegativeButton(R.string.str_cancel, null);
        dialog = builder.create();
        dialog.show();
    }

    public static void noUpdate(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.str_no_new_version);
        builder.setTitle(R.string.str_update);
        builder.setIcon(R.drawable.ic_update_blue);
        builder.setPositiveButton(R.string.str_confirm, null);
        builder.show();
    }

    public interface PermissionTool {
        void check();
    }

    public interface TransTool {
        void trans(Fragment fragment);
    }

    public interface TransToSearchTool {
        void trans(String q);
    }
}
