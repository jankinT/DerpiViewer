package com.Jankin.derpiviewer.settings;

public class Str {
    public static final String URL_HEAD = "https://derpibooru.org/api/v1/json/";
    public static final String FEATURED = "images/featured";
    public static final String DEFAULT = "&filter_id=100073";
    public static final String MAXIMUM_SPOILERS = "&filter_id=37430";
    public static final String EVERYTHING = "&filter_id=56027";
    public static final String _18PLUS_R34 = "&filter_id=37432";
    public static final String _18PLUS_DARK = "&filter_id=37429";
    public static final String LEGACY_DEFAULT = "&filter_id=37431";
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String SORT_BY_UPLOAD_DATE = "created_at";
    public static final String SORT_BY_FAVE_COUNT = "faves";
    public static final String SORT_BY_UPVOTES = "upvotes";
    public static final String SORT_BY_DOWNVOTES = "downvotes";
    public static final String SORT_BY_SCORE = "score";
    public static final String SORT_BY_COMMENTS = "comment_count";
    public static final String TRENDING = "search/images?q=first_seen_at.gt:3%20days%20ago&sf=wilson_score&sd=desc";
    public static final String SEARCH = "search/images?q=";

    public static final String[] SF_ARRAY = {
            SORT_BY_UPLOAD_DATE, SORT_BY_FAVE_COUNT, SORT_BY_UPVOTES, SORT_BY_DOWNVOTES,
            SORT_BY_SCORE, SORT_BY_COMMENTS
    };
    public static final String[] SD_ARRAY = {DESC, ASC};
}
