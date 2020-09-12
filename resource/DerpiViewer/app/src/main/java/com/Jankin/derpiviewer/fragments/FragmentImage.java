package com.Jankin.derpiviewer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentImage extends FragmentBase {
    @BindView(R.id.showed_image) ImageView showedImage;

    public FragmentImage(DPImage image, Context context) {
        super(image, context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image, container, false);
        init();
        setViews();

        return view;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this, view);
        super.init();
    }

    @Override
    protected void setViews() {
        super.setViews();
        setImage();
    }

    private void setImage() {
        RequestListener<Drawable> listener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                showedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setImage();
                    }
                });

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        };
        if((!image.isThumbnailsGenerated())&&(image.getFormat().equals("webm"))) {
            Glide.with(view).load(R.drawable.progressing).into(showedImage);
        } else if(image.isSpoilered()) {
            Glide.with(view).load(R.drawable.ic_tagblocked).into(showedImage);
            showedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(view).load(image.getViewUrl()).listener(listener)
                            .error(R.drawable.ic_loading_failed).into(showedImage);
                    v.setOnClickListener(null);
                }
            });
        } else {
            Glide.with(view).load(image.getViewUrl()).listener(listener)
                    .error(R.drawable.ic_loading_failed).into(showedImage);
        }
    }
}
