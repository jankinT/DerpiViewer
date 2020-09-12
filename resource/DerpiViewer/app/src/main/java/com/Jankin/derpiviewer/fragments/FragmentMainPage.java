package com.Jankin.derpiviewer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Jankin.derpiviewer.Adapters.ImagesAdapter;
import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.UniTool;
import com.Jankin.derpiviewer.tools.NetTool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMainPage extends Fragment implements View.OnClickListener {
    private final String TAG = "FragmentMainPage";
    private int page = 1;
    private View view;
    private FragmentManager manager;
    private FragmentLoading fragmentLoading;
    private FragmentImages fragmentImages;
    @BindView(R.id.featured_image) ImageView featuredImageView;
    @BindView(R.id.page_num) TextView pageNum;

    @BindView(R.id.last_page) FloatingActionButton lastPage;
    @BindView(R.id.next_page) FloatingActionButton nextPage;
    private Context context;
    private DPImage featuredImage;


    public FragmentMainPage(Context context, DPImage featuredImage) {
        this.context = context;
        this.featuredImage = featuredImage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_main_page, container, false);

        init();

        lastPage.setOnClickListener(this);

        nextPage.setOnClickListener(this);

        featuredImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        AppData.setMainPageVisible(true);
        if(AppData.isMainFirstTime()) {
            setImages();
        } else {
            resetImages();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        AppData.setMainPageVisible(false);
    }

    private void init() {
        ButterKnife.bind(this, view);
        Glide.with(view).load(featuredImage.getViewUrl()).error(R.drawable.ic_loading_failed)
                .override(Target.SIZE_ORIGINAL).into(featuredImageView);
        manager = getFragmentManager();
    }

    private void trans(Fragment fragment) {
        manager.beginTransaction().replace(R.id.main_content, fragment).commit();
    }

    private void setImages() {
        if(fragmentLoading == null) {
            fragmentLoading = new FragmentLoading();
        }
        trans(fragmentLoading);
        NetTool.getTrendingImages(page, dpImages -> {
            AppData.mainImages = dpImages;
            if(dpImages != null) {
                if(AppData.isMainPageVisible()) {
                    ImagesAdapter adapter = new ImagesAdapter(context, dpImages);
                    fragmentImages = new FragmentImages(context, adapter);
                    trans(fragmentImages);
                    String strPage = UniTool.getPageText(page);
                    pageNum.setText(strPage);
                } else {
                    Log.d(TAG, "setImages: FragmentMainPage not visible");
                }
                AppData.notMainFirstTime();
            } else {
                if(AppData.isMainPageVisible()) {
                    trans(new FragmentError());
                }
            }
        });
    }

    private void resetImages() {
        if(AppData.mainImages != null) {
            if(AppData.isMainPageVisible()) {
                ImagesAdapter adapter = new ImagesAdapter(context, AppData.mainImages);
                fragmentImages = new FragmentImages(context, adapter, AppData.mainIndex);
                trans(fragmentImages);
                String strPage = UniTool.getPageText(page);
                pageNum.setText(strPage);
            } else {
                Log.d(TAG, "setImages: FragmentMainPage not visible");
            }
            AppData.mainIndex = -1;
        } else {
            if(AppData.isMainPageVisible()) {
                trans(new FragmentError());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(!AppData.isLoading()) {
            switch (v.getId()) {
                case R.id.last_page:
                    if(page > 1) {
                        page--;
                        setImages();
                    }
                    break;
                case R.id.next_page:
                    if(AppData.mainImages.size() >= AppData.IMAGES_SIZE) {
                        page++;
                        setImages();
                    }
                    break;
                case R.id.featured_image:
                    AppData.setImageShowing(true);
                    if(featuredImage.getFormat().equals("webm")) {
                        FragmentVideo fragmentVideo = new FragmentVideo(featuredImage, context);
                        UniTool.trans(fragmentVideo);
                    } else{
                        FragmentImage fragmentImage = new FragmentImage(featuredImage, context);
                        UniTool.trans(fragmentImage);
                    }
                    break;
            }
        } else {
            Toast.makeText(context, R.string.str_loading, Toast.LENGTH_SHORT).show();
        }
    }
}
