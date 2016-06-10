package com.example.nikola.finalmovieapp.movieData;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nikola.finalmovieapp.BuildConfig;
import com.example.nikola.finalmovieapp.parcelable.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchMovieInfoTask extends AsyncTask<String, Void, Movie> {

    private static final String TAG = "FetchMovieInfoTask";

    private Movie getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

        final String COLUMN_BACKDROP = "backdrop_path";
        final String COLUMN_OVERVIEW = "overview";
        final String COLUMN_POSTER = "poster_path";
        final String COLUMN_RELEASE_DATE = "release_date";
        final String COLUMN_VOTE_AVERAGE = "vote_average";
        final String COLUMN_VOTE_COUNT = "vote_count";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        String backdrop = moviesJson.getString(COLUMN_BACKDROP);
        String plot = moviesJson.getString(COLUMN_OVERVIEW);
        String poster = moviesJson.getString(COLUMN_POSTER);
        String releaseDate = moviesJson.getString(COLUMN_RELEASE_DATE);
        String rating = moviesJson.getString(COLUMN_VOTE_AVERAGE);
        String voteCount = moviesJson.getString(COLUMN_VOTE_COUNT);

        return new Movie(backdrop, plot, poster, releaseDate, rating, voteCount);
    }

    protected Movie doInBackground(String... params) {

        String api = BuildConfig.TMDB_API_KEY;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String API_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(params[0])
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
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}












