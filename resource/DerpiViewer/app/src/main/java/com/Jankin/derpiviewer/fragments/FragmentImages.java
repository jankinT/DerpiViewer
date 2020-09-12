package com.Jankin.derpiviewer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Jankin.derpiviewer.Adapters.ImagesAdapter;
import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.deco.Deco;

public class FragmentImages extends Fragment {
    private Context context;
    private ImagesAdapter adapter;
    private RecyclerView imageList;
    private int index = -1;

    public FragmentImages(Context context, ImagesAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    public FragmentImages(Context context, ImagesAdapter adapter, Integer index) {
        this.context = context;
        this.adapter = adapter;
        this.index = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imageList = (RecyclerView) inflater.inflate(R.layout.images_layout, container,
                false);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        imageList.setLayoutManager(manager);
        imageList.setAdapter(adapter);
        imageList.addItemDecoration(new Deco());
        if(index != -1) {
            imageList.scrollToPosition(index);
        }

        return imageList;
    }

    public void scrollToTop() {
        imageList.scrollToPosition(0);
    }
}
