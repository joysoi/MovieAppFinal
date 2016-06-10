package com.example.nikola.finalmovieapp.movieData;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.nikola.finalmovieapp.BuildConfig;
import com.example.nikola.finalmovieapp.parcelable.Movie;

public class FetchMovieTrailerTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private static final String TAG = "FetchMovieVideoTask";

    ArrayList<Movie> video = new ArrayList<>();

    private ArrayList<Movie> getMoviesVideoFromJson(String moviesJsonStr) throws JSONException {


        final String RESULT = "results";
        final String TMDB_KEY = "key";
        final String TMDB_SITE = "site";

        JSONObject vidListJson = new JSONObject(moviesJsonStr);
        JSONArray singleVideo = vidListJson.getJSONArray(RESULT);
        for (int i = 0; i < singleVideo.length(); i++) {
            JSONObject jVideo = singleVideo.getJSONObject(i);
            String key = jVideo.getString(TMDB_KEY);
            String site = jVideo.getString(TMDB_SITE);
            Movie m = new Movie(key, site);
            video.add(m);
        }

        return video;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        String api = BuildConfig.TMDB_API_KEY;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {
            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/movie";
            final String VIDEO_PATH = "videos";
            final String API_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendPath(VIDEO_PATH)
                    .appendQueryParameter(API_PARAM, api)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesVideoFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
