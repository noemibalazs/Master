package com.example.android.master.loader;


import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.master.data.Article;
import com.example.android.master.remote.ArticleUtils;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;

    public ArticleLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        if (mUrl == null ){
            return null;
        }
        List<Article> articles = ArticleUtils.fetchData(mUrl);
        return articles;
    }
}

