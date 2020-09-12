package com.Jankin.derpiviewer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentVideo extends FragmentBase {
    @BindView(R.id.display) VideoView display;
    private MediaController controller;

    public FragmentVideo(DPImage image, Context context) {
        super(image, context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        init();
        setViews();

        return view;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this, view);
        super.init();
        controller = new MediaController(context);
        controller.setAnchorView(display);
    }

    @Override
    protected void setViews() {
        super.setViews();
        display.setVideoURI(Uri.parse(image.getRepresentations().get(DPImage.SMALL)));
        display.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                tagsView.setClickable(true);
            }
        });
        tagsView.setClickable(false);
        display.start();
        display.setMediaController(controller);
        display.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                display.resume();
                display.start();
            }
        });
    }

}
