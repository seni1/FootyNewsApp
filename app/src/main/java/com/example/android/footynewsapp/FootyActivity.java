package com.example.android.footynewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FootyActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Footy>> {

    private static final String FOOTY_REQUEST_URL = "http://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 1;

    private FootyAdapter adapter;
    private FootyLoader loader;

    private TextView emptyStateTextView;
    private ListView footyListView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private static final String PAGE_SIZE = "page-size";
    private static final String API_KEY = "api-key";
    private static final String KEY = "14153493-2fcc-42d8-9cc6-5a202222e671";
    private static final String SHOW_FIELDS = "show-fields";
    private static final String BYLINE = "byline";
    private static final String NONE = "none";
    private static final String SECTION = "section";
    private static final String ORDER_BY = "order-by";
    private static final String NEWEST = "newest";
    private static final String RELEVANCE = "relevance";
    private static final String QUERY = "q";




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
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            loadingIndicator.setVisibility(View.GONE);
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
        return new FootyLoader(this, searchResult(null));
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

    @Override

    //This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String searchResult(String query) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Uri baseUri = Uri.parse(FOOTY_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));
        if (TextUtils.isEmpty(pageSize)) {
            pageSize = "0";
        }
        uriBuilder.appendQueryParameter(PAGE_SIZE, pageSize);
        uriBuilder.appendQueryParameter(API_KEY, KEY);
        uriBuilder.appendQueryParameter(SHOW_FIELDS, BYLINE);
        String section = sharedPreferences.getString(getString(R.string.settings_only_show_key), getString(R.string.settings_only_show_default));
        if (!section.equals(NONE)) {
            uriBuilder.appendQueryParameter(SECTION, section);
        }

        if (query == null) {
            uriBuilder.appendQueryParameter(ORDER_BY, NEWEST);
            return uriBuilder.toString();
        }
        uriBuilder.appendQueryParameter(ORDER_BY, RELEVANCE);
        uriBuilder.appendQueryParameter(QUERY, query);

        return uriBuilder.toString();
    }

}
