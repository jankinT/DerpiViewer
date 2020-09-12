package com.Jankin.derpiviewer.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.fragments.FragmentImage;
import com.Jankin.derpiviewer.fragments.FragmentVideo;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.UniTool;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {
    private Context context;
    private List<DPImage> images;

    public ImagesAdapter(Context context, List<DPImage> images) {
        this.context = context;
        this.images = images;
    }

    public void setImages(List<DPImage> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.image_item, parent, false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        final DPImage image = images.get(position);

        holder.favNum.setText(String.valueOf(image.getFaves()));
        holder.score.setText(String.valueOf(image.getScore()));
        holder.commentNum.setText(String.valueOf(image.getCommentCount()));
        if(!image.isThumbnailsGenerated()){
            Glide.with(holder.itemView).load(R.drawable.progressing).into(holder.thumbNail);
        } else if(image.isSpoilered()) {
            Glide.with(holder.itemView).load(R.drawable.ic_tagblocked)
                    .error(R.drawable.loading_failed).into(holder.thumbNail);
        } else {
            Glide.with(holder.itemView).load(image.getRepresentations().get("thumb"))
                    .error(R.drawable.ic_loading_failed).into(holder.thumbNail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.setImageShowing(true);
                Fragment fragment;
                if(image.isThumbnailsGenerated()) {
                    if (image.getFormat().equals("webm")) {
                        fragment = new FragmentVideo(image, context);
                    } else {
                        fragment = new FragmentImage(image, context);
                    }
                } else {
                    fragment = new FragmentImage(image, context);
                }
                switch (AppData.currentShowing) {
                    case AppData.MAIN:
                        AppData.mainIndex = position;
                        break;
                    case AppData.SEARCH:
                        AppData.searchedIndex = position;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: "
                                + AppData.currentShowing);
                }
                UniTool.trans(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView thumbNail;
        TextView favNum;
        TextView score;
        TextView commentNum;


        ImageHolder(@NonNull View itemView) {
            super(itemView);
            thumbNail = itemView.findViewById(R.id.image_thumb);
            favNum = itemView.findViewById(R.id.fav_num);
            score = itemView.findViewById(R.id.score_num);
            commentNum = itemView.findViewById(R.id.comments_num);
        }

    }
}
