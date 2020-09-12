package com.Jankin.derpiviewer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.Jankin.derpiviewer.Adapters.ImagesAdapter;
import com.Jankin.derpiviewer.R;
import com.Jankin.derpiviewer.beans.DPImage;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.Str;
import com.Jankin.derpiviewer.settings.UniTool;
import com.Jankin.derpiviewer.tools.NetTool;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentSearch extends Fragment implements View.OnClickListener {
    private final String TAG = "FragmentSearch";
    private int page = 1;
    private View view;
    private FragmentImages fragmentImages;
    private FragmentManager manager;
    private FragmentLoading fragmentLoading;
    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.search_button) ImageButton searchButton;
    @BindView(R.id._sf) Spinner spinnerSf;
    @BindView(R.id._sd) Spinner spinnerSd;
    private ArrayAdapter<String> sfAdapter;
    private ArrayAdapter<String> sdAdapter;
    @BindView(R.id.page_num) TextView pageNum;
    @BindView(R.id.last_page) FloatingActionButton lastPage;
    @BindView(R.id.next_page) FloatingActionButton nextPage;
    private Context context;

    public FragmentSearch(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        init();

        searchButton.setOnClickListener(this);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(!AppData.isLoading()) {
                        search();
                        return true;
                    } else {
                        Toast.makeText(context, R.string.str_loading, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });

        lastPage.setOnClickListener(this);

        nextPage.setOnClickListener(this);

        spinnerSf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppData.sf = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppData.sd = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        AppData.setSearchVisible(true);
        if(AppData.isSearchFirstTime()) {
            search();
        } else {
            resetImages();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        AppData.setSearchVisible(false);
    }

    private void init() {
        ButterKnife.bind(this, view);
        String[] sfNames = context.getResources().getStringArray(R.array.str_array_sf_names);
        String[] sdNames = context.getResources().getStringArray(R.array.str_array_sd_names);
        sfAdapter = new ArrayAdapter<>(context, R.layout.layout_spinner_item,
                R.id.spinner_text, sfNames);
        sdAdapter = new ArrayAdapter<>(context, R.layout.layout_spinner_item,
                R.id.spinner_text, sdNames);
        spinnerSf.setAdapter(sfAdapter);
        spinnerSd.setAdapter(sdAdapter);
        spinnerSd.setSelection(AppData.sd);
        spinnerSf.setSelection(AppData.sf);
        searchInput.setText(AppData.q);
        manager = getFragmentManager();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last_page:
                if (page > 1) {
                    page--;
                    search();
                }
                break;
            case R.id.next_page:
                if (AppData.searchedImages.size() >= AppData.IMAGES_SIZE) {
                    page++;
                    search();
                }
                break;
            case R.id.search_button:
                page = 1;
                search();
                break;
        }
    }

    private void search() {
        Log.d(TAG, "search");
        if(fragmentLoading == null) {
            fragmentLoading = new FragmentLoading();
        }
        trans(fragmentLoading);
        String q = searchInput.getText().toString().trim();
        searchInput.setText(q);
        AppData.q = q;
        if(q.equals("")) {
            q = "*";
        }
        String sf = Str.SF_ARRAY[AppData.sf];
        String sd = Str.SD_ARRAY[AppData.sd];
        NetTool.getSearchedImages(q, page, sf, sd, dpImages -> {
            AppData.searchedImages = dpImages;
            if(dpImages != null) {
                if(AppData.isSearchVisible()) {
                    ImagesAdapter adapter = new ImagesAdapter(context, dpImages);
                    fragmentImages = new FragmentImages(context, adapter);
                    trans(fragmentImages);
                    String strPage = UniTool.getPageText(page);
                    pageNum.setText(strPage);
                    AppData.notSearchFirstTime();
                } else {
                    Log.d(TAG, "search: FragmentSearch not visible");
                }
            } else {
                if(AppData.isSearchVisible()) {
                    trans(new FragmentError());
                }
            }
        });
    }

    private void resetImages() {
        Log.d(TAG, "resetImages");
        searchInput.setText(AppData.q);
        if(AppData.searchedImages != null) {
            if(AppData.isSearchVisible()) {
                ImagesAdapter adapter = new ImagesAdapter(context, AppData.searchedImages);
                fragmentImages = new FragmentImages(context, adapter, AppData.searchedIndex);
                trans(fragmentImages);
                String strPage = UniTool.getPageText(page);
                pageNum.setText(strPage);
            } else {
                Log.d(TAG, "resetImage: FragmentSearch not visible");
            }
            AppData.searchedIndex = -1;
        } else {
            if(AppData.isSearchVisible()) {
                trans(new FragmentError());
            }
        }
    }

    private void trans(Fragment fragment) {
        manager.beginTransaction().replace(R.id.search_content, fragment).commit();
    }
}
