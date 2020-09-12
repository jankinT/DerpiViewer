package com.Jankin.derpiviewer.fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Jankin.derpiviewer.Adapters.CommentsAdapter;
import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPComment;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.deco.Deco;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.UniTool;
import com.Jankin.derpiviewer.tasks.DownloadTask;
import com.Jankin.derpiviewer.tasks.DownloadTaskQ;
import com.Jankin.derpiviewer.tools.NetTool;
import com.ismaeltoe.FlowLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class FragmentBase extends Fragment {
    protected DPImage image;
    protected Context context;
    protected final int COMMENTS_SIZE = 25;

    protected View view;
    @BindView(R.id.image_fav_num) TextView favNum;
    @BindView(R.id.image_up_num) TextView upvotes;
    @BindView(R.id.image_down_num) TextView downvotes;
    @BindView(R.id.image_score) TextView score;
    @BindView(R.id.image_comment_num) TextView commentNum;
    @BindView(R.id.image_uploader) TextView uploader;
    @BindView(R.id.image_info) TextView info;
    @BindView(R.id.image_source) TextView source;
    @BindView(R.id.image_description) TextView description;
    @BindView(R.id._download) LinearLayout download;
    @BindView(R.id._share) LinearLayout share;
    @BindView(R.id._tags) FlowLayout tagsView;
    @BindView(R.id.comments_view) RecyclerView commentsView;
    protected int page = 1;
    protected CommentsAdapter commentsAdapter;
    protected ArrayList<DPComment> comments;
    protected ImageButton lastPage, nextPage;
    protected TextView pageNumber;
    protected LinearLayoutManager unScrollable;

    public FragmentBase(DPImage image, Context context) {
        this.image = image;
        this.context = context;
    }

    protected void setViews() {
        favNum.setText(String.valueOf(image.getFaves()));
        upvotes.setText(String.valueOf(image.getUpvotes()));
        downvotes.setText(String.valueOf(image.getDownvotes()));
        score.setText(String.valueOf(image.getScore()));
        commentNum.setText(String.valueOf(image.getCommentCount()));
        if(image.getUploader() != null) {
            uploader.setText(image.getUploader());
        } else {
            uploader.setText(R.string.str_unknown);
        }

        String strInfo = "" + image.getWidth() + " × " + image.getHeight() + "  " +
                image.getFormat();
        info.setText(strInfo);

        String strSource = context.getResources().getString(R.string.str_source);
        if(image.getSourceUrl() != null) {
            strSource += image.getSourceUrl();
        } else {
            strSource += context.getResources().getString(R.string.str_none);
        }

        source.setText(strSource);

        String strDescription = context.getResources().getString(R.string.str_description)
                + image.getDescription();
        description.setText(strDescription);

        download.setOnClickListener(v -> downloads());
        share.setOnClickListener(v -> shares());
        for(String tag : image.getTags()) {
            TextView tagText = (TextView) View.inflate(context, R.layout.tag_view, null);
            tagText.setText(tag);
            tagText.setOnClickListener(v -> UniTool.doSearch(tag));
            tagsView.addView(tagText);
        }
        setCommentsView(page);
        lastPage.setOnClickListener(v -> {
            if(page - 1 > 0) {
                page--;
                setCommentsView(page);
            }
        });
        nextPage.setOnClickListener(v -> {
            if(comments != null && comments.size() >= COMMENTS_SIZE) {
                page++;
                setCommentsView(page);
            }
        });
    }

    protected void init() {
        unScrollable = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        unScrollable.setOrientation(RecyclerView.VERTICAL);
        commentsView.setLayoutManager(unScrollable);
        commentsView.addItemDecoration(new Deco());
        lastPage = view.findViewById(R.id.last_page);
        nextPage = view.findViewById(R.id.next_page);
        pageNumber = view.findViewById(R.id.page_num);
    }

    protected void downloads() {
        if(UniTool.isPermitted(context)) {
            Toast.makeText(context, R.string.str_downloading_start, Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                DownloadTaskQ taskQ = new DownloadTaskQ(context);
                taskQ.execute(image);
            } else {
                DownloadTask task = new DownloadTask(context);
                task.execute(image);
            }
        } else {
            UniTool.checkPermissions();
        }
    }

    protected void shares() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_share);
        builder.setTitle(R.string.str_share);
        String[] strArray = context.getResources().getStringArray(R.array.str_array_share);
        builder.setItems(strArray, (dialog, which) -> {
            ClipboardManager manager = AppData.getClipboardManager();
            ClipData data;
            if(which == 0) {
                data = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN,
                        image.getViewUrl());
                manager.setPrimaryClip(data);
                Toast.makeText(context, R.string.str_copy_succeed, Toast.LENGTH_SHORT).show();
            } else if(which == 1) {
                if(image.getSourceUrl() != null) {
                    data = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN,
                            image.getSourceUrl());
                    manager.setPrimaryClip(data);
                    Toast.makeText(context, R.string.str_copy_succeed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.str_no_source, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

    protected void setCommentsView(final int page) {
        NetTool.getComments(image.getId(), page, dpComments -> {
            if(dpComments != null && isVisible()) {
                comments = dpComments;
                commentsAdapter = new CommentsAdapter(context, comments);
                commentsView.setAdapter(commentsAdapter);
                commentsAdapter.notifyDataSetChanged();
                String strPage = "" + page;
                pageNumber.setText(strPage);
            } else if(isVisible()) {
                Toast.makeText(context, R.string.str_loading_comment_failed, Toast.LENGTH_SHORT)
                        .show();
            } else{
                Log.d("Comments", "setCommentsView: not visible");
            }
        });
    }
}
