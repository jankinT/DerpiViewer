package com.Jankin.derpiviewer.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.Jankin.derpiviewer.MainActivity;
import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.FilterSet;
import com.Jankin.derpiviewer.settings.Str;
import com.Jankin.derpiviewer.tasks.NetTask;
import com.Jankin.derpiviewer.tasks.TaskAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentFilterSet extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private View view;
    @BindView(R.id.filter_group) RadioGroup group;
    @BindView(R.id.filter_info) TextView filterInfo;

    private Context context;

    public FragmentFilterSet(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_set, container, false);

        init();
        group.check(AppData.filterSet);
        setFilter(AppData.filterSet);
        group.setOnCheckedChangeListener(this);

        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setFilter(checkedId);
    }

    private void setFilter(int id) {
        AppData.load();
        FilterSet set;
        AppData.filterSet = id;
        SharedPreferences preferences = context.getSharedPreferences(
                "derpi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("filterId", id);
        switch (id) {
            default:
                set = FilterSet.DEFAULT;
                break;
            case R.id.filter_maximum_spoiler:
                set = FilterSet.MAXIMUM_SPOILERS;
                break;
            case R.id.filter_everything:
                set = FilterSet.EVERYTHING;
                break;
            case R.id.filter_18plus_r34:
                set = FilterSet._18PLUS_R34;
                break;
            case R.id.filter_18plus_dark:
                set = FilterSet._18PLUS_DARK;
                break;
            case R.id.filter_legacy_default:
                set = FilterSet.LEGACY_DEFAULT;
                break;
        }
        filterInfo.setText(set.getInfo());
        AppData.filter = set.getFilter();
        filterInfo.setText(set.getInfo());
        AppData.filter = set.getFilter();
        editor.putString("filter", set.getFilter());
        editor.apply();
        AppData.init();
        AppData.loadingFinish();
    }
}
