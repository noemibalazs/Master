package com.example.android.master.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ArticleContract {

    public ArticleContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.reader";
    public static final String ARTICLE_PATH = "articles";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, ARTICLE_PATH);

        public static final String ARTICLE_TABLE = "article_table";

        public static final String DYR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ARTICLE_PATH;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ARTICLE_PATH;

        public static final String ID = BaseColumns._ID;
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String BODY = "body";
        public static final String THUMB = "thumb";
        public static final String PHOTO = "photo";
        public static final String DATE = "date";
    }
}
