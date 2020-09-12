package com.Jankin.derpiviewer.settings;

import android.content.ClipboardManager;

import com.Jankin.derpiviewer.beans.DPImage;

import java.util.List;

public class AppData {
    public static final int IMAGES_SIZE = 15;
    public static DPImage featuredImage;
    public static List<DPImage> mainImages, searchedImages;
    public static boolean mainFirstTime = true, searchFirstTime = true;
    public static String filter;
    public static int filterSet;
    public static String q = "";
    public static int sd = 0;
    public static int sf = 0;
    public static int language = Language.EN;
    private static boolean loading = false;
    private static boolean imageShowing = false;
    private static boolean mainPageVisible = false;
    private static boolean searchVisible = false;
    public static int mainIndex = -1, searchedIndex = -1;
    public static int currentShowing = 0;
    public static final int NONE = -1, MAIN = 0, SEARCH = 1;
    private static ClipboardManager clipboardManager;

    public static boolean isImageShowing() {
        return imageShowing;
    }

    public static void setImageShowing(boolean imageShowing) {
        AppData.imageShowing = imageShowing;
    }

    public static void setClipboardManager(ClipboardManager clipboardManager) {
        AppData.clipboardManager = clipboardManager;
    }

    public static ClipboardManager getClipboardManager() {
        return clipboardManager;
    }

    public static void load() {
        loading = true;
    }

    public static void loadingFinish() {
        loading = false;
    }

    public static boolean isLoading() {
        return loading;
    }

    public static void init() {
        mainFirstTime = true;
        searchFirstTime = true;
        mainIndex = -1;
    }

    public static void searchInit() {
        searchFirstTime = true;
        searchedIndex = -1;
    }

    public static boolean isMainFirstTime() {
        return mainFirstTime;
    }

    public static boolean isSearchFirstTime() {
        return searchFirstTime;
    }

    public static void notMainFirstTime() {
        mainFirstTime = false;
    }

    public static void notSearchFirstTime() {
        searchFirstTime = false;
    }

    public static void setMainPageVisible(boolean flag) {
        mainPageVisible = flag;
    }

    public static boolean isMainPageVisible() {
        return mainPageVisible;
    }

    public static void setSearchVisible(boolean flag) {
        AppData.searchVisible = flag;
    }

    public static boolean isSearchVisible() {
        return searchVisible;
    }
}
