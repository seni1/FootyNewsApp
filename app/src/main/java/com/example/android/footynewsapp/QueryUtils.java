package com.example.android.footynewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting earthquake data from Guardian API.
 */

public class QueryUtils {


    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Footy> extractFeatureFromJson(String footballJSON) {

        if (TextUtils.isEmpty(footballJSON)) {
            return null;
        }

        List<Footy> allFootball = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(footballJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray results = response.getJSONArray("results");

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < response.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);

                String type = currentNews.getString("type");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String title = currentNews.getString("webTitle");
                String url = currentNews.getString("webUrl");

                JSONObject fields = currentNews.getJSONObject("fields");
                String author = fields.getString("byline");


                String dateFormat;

                if (date == null) {
                    dateFormat = " ";
                } else {
                    dateFormat = date.substring(11, 16) + "    " + date.substring(0, 10);
                    dateFormat = dateFormat.replaceAll("-", ".");
                }

                Footy football = new Footy(type, section, dateFormat, title, url, author);

                allFootball.add(football);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        //Return the list of news
        return allFootball;
    }

    public static List<Footy> fetchNewsData(String requestUrl) {

        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        //Extract relevant fields from the JSON response and create a list of {@link Football}
        List<Footy> allFooty = extractFeatureFromJson(jsonResponse);

        //Return the list of {@link Football}
        return allFooty;
    }

}
