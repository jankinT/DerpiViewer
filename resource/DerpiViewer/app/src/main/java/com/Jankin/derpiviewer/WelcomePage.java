package com.Jankin.derpiviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.Jankin.derpiviewer.fragments.ErrorNoNetWork;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.Language;
import com.Jankin.derpiviewer.settings.Str;
import com.Jankin.derpiviewer.settings.UniTool;
import com.Jankin.derpiviewer.tools.NetTool;

import java.util.Locale;

public class WelcomePage extends AppCompatActivity {
    final String TAG = "WelcomePage";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = this.getSharedPreferences("derpi" ,MODE_PRIVATE);
        AppData.language = preferences.getInt("language", Language.EN);
        UniTool.changeLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        AppData.filter = preferences.getString("filter", Str.DEFAULT);
        AppData.filterSet = preferences.getInt("filterId", R.id.filter_default);


        NetTool.getFeaturedPicture(dpImage -> {
            if(dpImage != null) {
                AppData.featuredImage = dpImage;
                Intent intent = new Intent();
                intent.setClass(WelcomePage.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setClass(WelcomePage.this, ErrorNoNetWork.class);
                startActivity(intent);
            }
            finish();
        }, aVoid -> {
            Toast.makeText(WelcomePage.this, R.string.str_click_on_the_button,
                    Toast.LENGTH_SHORT).show();
            String url = "https://derpibooru.org/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
