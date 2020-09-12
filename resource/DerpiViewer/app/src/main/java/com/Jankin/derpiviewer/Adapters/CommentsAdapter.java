package com.Jankin.derpiviewer.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPComment;
import com.bumptech.glide.Glide;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {
    private final String TAG = "CommentsAdapter";
    private Context context;
    private ArrayList<DPComment> comments;

    public CommentsAdapter(Context context, ArrayList<DPComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.layout_comment, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        DPComment comment = comments.get(position);
        String localTime = null;
        String utc = comment.getCreatedAt();
        utc = utc.replace("T", " ");
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        try {
            Date utcDate = utcFormat.parse(utc);
            DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
            assert utcDate != null;
            localTime = dateFormat.format(utcDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            localTime = utc;
        }
        String uploadDate = context.getResources().getString(R.string.str_upload_date);
        uploadDate += ": " + localTime;
        holder.authorName.setText(comment.getAuthor());
        holder.commentContent.setText(comment.getBody());
        holder.uploadDate.setText(uploadDate);
        if(comment.getAvatar().startsWith("data:image/svg+xml;base64,")) {
            String str = comment.getAvatar().split(",")[1];
            byte[] bytes = Base64.decode(str, Base64.DEFAULT);
            try {
                SVG svg = SVG.getFromString(new String(bytes));
                Drawable drawable = new PictureDrawable(svg.renderToPicture());
                Glide.with(holder.itemView).load(drawable).error(R.drawable.loading_failed).
                        into(holder.avatarImage);
            } catch (SVGParseException e) {
                e.printStackTrace();
            }
        } else {
            Glide.with(holder.itemView).load(comment.getAvatar())
                    .error(R.drawable.loading_failed).into(holder.avatarImage);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView authorName, commentContent, uploadDate;
        CommentHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.avatar_image);
            authorName = itemView.findViewById(R.id.author_name);
            commentContent = itemView.findViewById(R.id.comment_content);
            uploadDate = itemView.findViewById(R.id.comment_time);
        }
    }
}
