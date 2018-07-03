package com.example.android.footynewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FootyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Footy>> {

    private static final String FOOTY_REQUEST_URL = "https://content.guardianapis.com/football?api-key=14153493-2fcc-42d8-9cc6-5a202222e671";
    private static final int NEWS_LOADER_ID = 1;

    private FootyAdapter adapter;
    private FootyLoader loader;

    private TextView emptyStateTextView;
    private ListView footyListView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footy);

        footyListView = findViewById(R.id.list);
        emptyStateTextView = findViewById(R.id.empty_view);
        footyListView.setEmptyView(emptyStateTextView);

        adapter = new FootyAdapter(this, new ArrayList<Footy>());
        footyListView.setAdapter(adapter);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        footyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Footy currentNews = adapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                Intent footyIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(footyIntent);
            }

        });

        loader = (FootyLoader) getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Footy>> onCreateLoader(int id, Bundle bundle) {
        return new FootyLoader(this, FOOTY_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Footy>> loader, List<Footy> footies) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        emptyStateTextView.setText(R.string.no_news);

        adapter.clear();

        if (footies != null && !footies.isEmpty()) {
            adapter.addAll(footies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Footy>> loader) {
        adapter.clear();
    }

}
