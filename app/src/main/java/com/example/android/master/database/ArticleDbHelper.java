package com.example.android.master.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.master.database.ArticleContract.ArticleEntry;

public class ArticleDbHelper extends SQLiteOpenHelper {

    private static final String ARTICLE_DB = "article.db";
    private static final int ARTICLE_VERSION = 1;
    private static final String PRAGMA_FOREIGN_KEYS_ON = "PRAGMA foreign_keys=ON; ";

    public ArticleDbHelper(Context context){
        super(context, ARTICLE_DB, null, ARTICLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticleEntry.ARTICLE_TABLE + " ("
                + ArticleEntry.ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE, "
                + ArticleEntry.TITLE + " TEXT NOT NULL, "
                + ArticleEntry.AUTHOR + " TEXT NOT NULL, "
                + ArticleEntry.BODY + " TEXT NOT NULL, "
                + ArticleEntry.THUMB + " TEXT NOT NULL, "
                + ArticleEntry.PHOTO + " TEXT NOT NULL, "
                + ArticleEntry.DATE + " TEXT NOT NULL )";
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(PRAGMA_FOREIGN_KEYS_ON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.ARTICLE_TABLE);
        onCreate(db);

    }
}

