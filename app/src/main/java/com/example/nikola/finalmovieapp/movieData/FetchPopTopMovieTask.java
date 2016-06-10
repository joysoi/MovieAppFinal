package com.example.nikola.finalmovieapp.movieData;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.nikola.finalmovieapp.BuildConfig;
import com.example.nikola.finalmovieapp.parcelable.Movie;
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


public class FetchPopTopMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private static final String SORT_POPULAR = "popularity.desc";
    private static final String SORT_TOP = "vote_average.desc";
    ArrayList<Movie> resultStrs = new ArrayList<>();

    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {


        final String COLUMN_LIST = "results";
        final String COLUMN_ID = "id";
        final String COLUMN_IMG = "poster_path";
        final String COLUMN_TITLE = "title";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieList = moviesJson.getJSONArray(COLUMN_LIST);

        for (int i = 0; i < movieList.length(); i++) {
            JSONObject singleMovie = movieList.getJSONObject(i);
            String id = singleMovie.getString(COLUMN_ID);
            String img = singleMovie.getString(COLUMN_IMG);
            String title = singleMovie.getString(COLUMN_TITLE);

            Movie movieInfo = new Movie(id, img, title);
            resultStrs.add(movieInfo);
        }
        return resultStrs;
    }

    protected ArrayList<Movie> doInBackground(String... params) {

        String api = BuildConfig.TMDB_API_KEY;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;


        try {
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT = "sort_by";
            final String API = "api_key";
            final String VOTE_MIN = "vote_count.gte";
            final String VOTE_MIN_VALUE = "1000";
            String sort = params[0];

            Uri builtUri = null;
            switch (sort) {
                case SORT_POPULAR:
                    builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                            .appendQueryParameter(SORT, params[0])
                            .appendQueryParameter(API, api)
                            .build();
                    break;
                case SORT_TOP:
                    builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                            .appendQueryParameter(SORT, params[0])
                            .appendQueryParameter(API, api)
                            .appendQueryParameter(VOTE_MIN, VOTE_MIN_VALUE)
                            .build();
                    break;
            }

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
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
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


