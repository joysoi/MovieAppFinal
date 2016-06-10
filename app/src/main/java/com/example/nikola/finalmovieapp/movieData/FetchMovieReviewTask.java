package com.example.nikola.finalmovieapp.movieData;

import android.net.Uri;
import android.os.AsyncTask;

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
import com.example.nikola.finalmovieapp.parcelable.Review;


public class FetchMovieReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {

    ArrayList<Review> reviewList = new ArrayList<>();

    private ArrayList<Review> getMoviesReviewFromJson(String moviesJsonStr) throws JSONException {

        final String RESULT = "results";
        final String COLUMN_AUTHOR = "author";
        final String COLUMN_CONTENT = "content";


        JSONObject reviewListJson = new JSONObject(moviesJsonStr);
        JSONArray singleReview = reviewListJson.getJSONArray(RESULT);
        for (int i = 0; i < singleReview.length(); i++) {
            JSONObject rev = singleReview.getJSONObject(i);
            String author = rev.getString(COLUMN_AUTHOR);
            String content = rev.getString(COLUMN_CONTENT);
            Review review = new Review(author, content);
            reviewList.add(review);
        }

        return reviewList;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        String api = BuildConfig.TMDB_API_KEY;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {

            final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/movie";
            final String REVIEW = "reviews";
            final String API = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendPath(REVIEW)
                    .appendQueryParameter(API, api)
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
            return getMoviesReviewFromJson(moviesJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
