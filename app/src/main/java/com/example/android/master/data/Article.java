package com.example.android.master.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private int mID;
    private String mTitle;
    private String mAuthor;
    private String mBody;
    private String mThumb;
    private String mPhoto;
    private String mPublishedDate;

    public Article(int id, String title, String author, String body, String thumb, String photo, String publishedDate){
        mID = id;
        mTitle = title;
        mAuthor = author;
        mBody = body;
        mPhoto = photo;
        mThumb = thumb;
        mPublishedDate = publishedDate;
    }

    public int getArticleId(){
        return mID;
    }

    public String getArticleTitle(){
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getBody(){
        return mBody;
    }

    public String getArticleThumb(){
        return mThumb;
    }

    public String getArticlePhoto(){
        return mPhoto;
    }

    public String getPublishedDate(){
        return mPublishedDate;
    }

    private Article(Parcel in) {
        mID = in.readInt();
        mTitle = in.readString();
        mAuthor = in.readString();
        mBody = in.readString();
        mThumb = in.readString();
        mPhoto = in.readString();
        mPublishedDate = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mID);
        parcel.writeString(mTitle);
        parcel.writeString(mAuthor);
        parcel.writeString(mBody);
        parcel.writeString(mThumb);
        parcel.writeString(mPhoto);
        parcel.writeString(mPublishedDate);
    }
}

