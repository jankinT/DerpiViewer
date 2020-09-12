package com.Jankin.derpiviewer;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Jankin.derpiviewer.fragments.FragmentAbout;
import com.Jankin.derpiviewer.fragments.FragmentFilterSet;
import com.Jankin.derpiviewer.fragments.FragmentMainPage;
import com.Jankin.derpiviewer.fragments.FragmentSearch;
import com.Jankin.derpiviewer.settings.AppData;
import com.Jankin.derpiviewer.settings.UniTool;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = "Main";
    @BindView(R.id.tool_bar) Toolbar toolbar;
    @BindView(R.id._nav) NavigationView nav;
    @BindView(R.id._drawer) DrawerLayout drawer;
    private FragmentManager manager;
    private MenuItem currentMenu;
    private FragmentAbout fragmentAbout;
    private FragmentMainPage fragmentMainPage;
    private FragmentSearch fragmentSearch;
    private FragmentFilterSet fragmentFilterSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        fragmentMainPage = new FragmentMainPage(this, AppData.featuredImage);
        fragmentSearch = new FragmentSearch(this);
        fragmentAbout = new FragmentAbout();
        fragmentFilterSet = new FragmentFilterSet(this);
        UniTool.setPermissionTool(new UniTool.PermissionTool() {
            @Override
            public void check() {
                final String[] PERMISSIONS = {
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.WRITE_EXTERNAL_STORAGE" };

                int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE");

                if(permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            PERMISSIONS, 1);
                }
            }
        });
        UniTool.setTransTool(new UniTool.TransTool() {
            @Override
            public void trans(Fragment fragment) {
                manager.beginTransaction().addToBackStack(null).replace(R.id._content, fragment)
                        .commit();
            }
        });
        UniTool.setTransToSearchTool(new UniTool.TransToSearchTool() {
            @Override
            public void trans(String q) {
                if(!AppData.isLoading()) {
                    AppData.searchInit();
                    AppData.q = q;
                    MenuItem item = nav.getMenu().findItem(R.id.menu_search);
                    currentMenu.setChecked(false);
                    item.setChecked(true);
                    currentMenu = item;
                    fragmentSearch = new FragmentSearch(MainActivity.this);
                    fragmentTrans(fragmentSearch);
                    toolbar.setSubtitle(R.string.str_search);
                } else {
                    Toast.makeText(MainActivity.this, R.string.str_loading,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppData.setClipboardManager((ClipboardManager) getSystemService(CLIPBOARD_SERVICE));

        drawer.setScrimColor(getResources().getColor(R.color.colorShadow));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        manager = getSupportFragmentManager();
        nav.setNavigationItemSelectedListener(this);

        currentMenu = nav.getMenu().findItem(R.id.menu_main);
        currentMenu.setChecked(true);
        fragmentTrans(fragmentMainPage);
        toolbar.setSubtitle(R.string.str_main);
        UniTool.checkPermissions();
    }

    @Override
    public void onBackPressed() {
        if(AppData.isLoading()) {
            Toast.makeText(this, R.string.str_loading, Toast.LENGTH_SHORT).show();
        } else {
            if (drawer != null) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    if(AppData.isImageShowing()) {
                        super.onBackPressed();
                        AppData.setImageShowing(false);
                    } else {
                        finish();
                    }
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(AppData.isLoading()) {
            Toast.makeText(this, R.string.str_loading, Toast.LENGTH_SHORT).show();
            return false;
        }

        Fragment fragment;
        if(!menuItem.isChecked() && menuItem.getGroupId() == R.id.group_with_fragment) {
            switch (menuItem.getItemId()) {
                default:
                    AppData.currentShowing = AppData.NONE;
                    fragment = fragmentAbout;
                    toolbar.setSubtitle(R.string.str_about);
                    break;
                case R.id.menu_search:
                    AppData.currentShowing = AppData.SEARCH;
                    fragment = fragmentSearch;
                    toolbar.setSubtitle(R.string.str_search);
                    break;
                case R.id.menu_main:
                    AppData.currentShowing = AppData.MAIN;
                    fragment = fragmentMainPage;
                    toolbar.setSubtitle(R.string.str_main);
                    break;
                case R.id.menu_filter_set:
                    AppData.currentShowing = AppData.NONE;
                    fragment = fragmentFilterSet;
                    toolbar.setSubtitle(R.string.str_filter_set);
                    break;
            }
            if(currentMenu != null && currentMenu.isChecked()) {
                currentMenu.setChecked(false);
            }
            currentMenu = menuItem;
            menuItem.setChecked(true);
            fragmentTrans(fragment);
        } else{
            if(menuItem.getItemId() == R.id.menu_language) {
                changeLanguage();
            } else if(menuItem.getItemId() == R.id.menu_update) {
                Log.d(TAG, "onNavigationItemSelected: selected update");
                UniTool.checkUpdates(this);
            }
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void fragmentTrans(Fragment fragment) {
        AppData.setImageShowing(false);
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id._content, fragment);
        trans.commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void changeLanguage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] languages = getResources().getStringArray(R.array.str_array_language);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, languages);
        builder.setTitle(R.string.str_language);
        builder.setIcon(R.drawable.ic_language_blue);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which != AppData.language) {
                    getSharedPreferences("derpi", MODE_PRIVATE).edit()
                            .putInt("language", which).apply();
                    Intent intent = new Intent(MainActivity.this,
                            WelcomePage.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.create().show();
    }

}
