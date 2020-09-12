package com.Jankin.derpiviewer.settings;

import com.Jankin.derpiviewer.R;

public enum FilterSet {
    DEFAULT(R.string.str_default, R.string.info_default, Str.DEFAULT),
    MAXIMUM_SPOILERS(R.string.str_maximum_spoilers, R.string.info_maximum_spoilers,
            Str.MAXIMUM_SPOILERS),
    EVERYTHING(R.string.str_every_thing, R.string.info_every_thing, Str.EVERYTHING),
    _18PLUS_R34(R.string.str_18plus_r34, R.string.info_18plus_r34, Str._18PLUS_R34),
    _18PLUS_DARK(R.string.str_18plus_dark, R.string.info_18plus_dark, Str._18PLUS_DARK),
    LEGACY_DEFAULT(R.string.str_legacy_default, R.string.info_legacy_default, Str.LEGACY_DEFAULT);

    private int name;
    private int info;
    private String filter;

    FilterSet(int name, int info, String filter) {
        this.name = name;
        this.info = info;
        this.filter = filter;
    }

    public int getInfo() {
        return info;
    }

    public int getName() {
        return name;
    }

    public String getFilter() {
        return filter;
    }
}
