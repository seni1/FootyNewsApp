package com.example.android.footynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class FootyLoader extends AsyncTaskLoader<List<Footy>> {

    private String url;

    public FootyLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Footy> loadInBackground() {
        if (url == null) {
            return null;
        }

        List<Footy> news = QueryUtils.fetchNewsData(url);
        return news;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
