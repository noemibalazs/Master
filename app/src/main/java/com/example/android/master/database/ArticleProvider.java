package com.example.android.master.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.android.master.database.ArticleContract.ArticleEntry;

public class ArticleProvider extends ContentProvider {

    private static final String TAG = ArticleProvider.class.getSimpleName();

    private static final int ARTICLE = 111;
    private static final int ARTICLE_ITEM = 333;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.ARTICLE_PATH, ARTICLE);
        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.ARTICLE_PATH + "/#", ARTICLE_ITEM);
    }

    private ArticleDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ArticleDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLE:
                cursor = db.query(ArticleEntry.ARTICLE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case  ARTICLE_ITEM:
                selection = ArticleEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ArticleEntry.ARTICLE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        String table;

        switch (match){
            case ARTICLE:
                table = ArticleEntry.ARTICLE_TABLE;
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

        long id = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.v(TAG, "New row was inserted " + id );

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowDeleted;

        int match = sUriMatcher.match(uri);

        switch (match){
            case ARTICLE:
                rowDeleted = db.delete(ArticleEntry.ARTICLE_TABLE, selection, selectionArgs);
                if (rowDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;
            case ARTICLE_ITEM:
                selection = ArticleEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = db.delete(ArticleEntry.ARTICLE_TABLE, selection, selectionArgs);
                if (rowDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;

            default:
                throw  new IllegalArgumentException("Cannot delete row for" + uri);

        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowUpdate;

        switch (match){
            case ARTICLE:
                rowUpdate = db.update(ArticleEntry.ARTICLE_TABLE, values, selection, selectionArgs);
                if (rowUpdate!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowUpdate;
            case ARTICLE_ITEM:
                selection = ArticleEntry.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowUpdate = db.update(ArticleEntry.ARTICLE_TABLE, values, selection, selectionArgs);
                if (rowUpdate!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowUpdate;
            default:
                throw new IllegalArgumentException("Cannot update row for" + uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case ARTICLE:
                return ArticleEntry.DYR_TYPE;
            case ARTICLE_ITEM:
                return ArticleEntry.ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri "+ uri + "with match" + match);
        }
    }

}
