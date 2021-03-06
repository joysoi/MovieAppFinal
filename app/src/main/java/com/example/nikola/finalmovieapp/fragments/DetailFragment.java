package com.example.nikola.finalmovieapp.fragments;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikola.finalmovieapp.activities.ReviewActivity;
import com.example.nikola.finalmovieapp.adapters.MovieTrailerAdapter;
import com.example.nikola.finalmovieapp.dataBases.MovieContract;
import com.example.nikola.finalmovieapp.movieData.FetchMovieInfoTask;
import com.example.nikola.finalmovieapp.movieData.FetchMovieReviewTask;
import com.example.nikola.finalmovieapp.movieData.FetchMovieTrailerTask;
import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.parcelable.Review;
import com.example.nikola.finalmovieapp.R;
import com.example.nikola.finalmovieapp.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailFragment extends Fragment {

    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMoviesApp";
    private static final String TAG = "DetailFragment";
    private final String POSTER_SIZE_PATH = "w342";
    private final String BACKDROP_SIZE_PATH = "w780";

    CoordinatorLayout coordinatorLayout;
    ImageView imgPoster, imgBackdrop;
    TextView tvRating, tvRelease, tvPlot;
    TextView tvUsername, tvContent;
    Button btnMoreReview;
    LinearLayout reviewLayout;
    MovieTrailerAdapter movieTrailerAdapter;
    LinearLayout trailerLayout;

    private Boolean mTwoPane = false;
    private String id, title;
    private String mBackdrop, mPoster, mRating, mReleaseDate, mPlot;
    private ArrayList<Movie> trailerList = new ArrayList<>();
    private ArrayList<Review> reviewList = new ArrayList<>();

    public static DetailFragment newInstance(String id, String title, Boolean twoPane) {
        DetailFragment f = new DetailFragment();

        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("title", title);
        args.putBoolean("twoPane", twoPane);
        f.setArguments(args);

        return f;
    }

    public DetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (!getArguments().isEmpty()) {
            id = getArguments().getString("id");
            title = getArguments().getString("title");
            mTwoPane = getArguments().getBoolean("twoPane");

        }

        if (savedInstanceState != null) {
            id = savedInstanceState.getString(getString(R.string.key_id));
            title = savedInstanceState.getString(getString(R.string.key_title));
            mBackdrop = savedInstanceState.getString(getString(R.string.key_backdrop));
            mPoster = savedInstanceState.getString(getString(R.string.key_poster));
            mRating = savedInstanceState.getString(getString(R.string.key_rating));
            mReleaseDate = savedInstanceState.getString(getString(R.string.key_release_date));
            mPlot = savedInstanceState.getString(getString(R.string.key_plot));
            trailerList = savedInstanceState.getParcelableArrayList(getString(R.string.key_trailer));
            reviewList = savedInstanceState.getParcelableArrayList(getString(R.string.key_review));
        } else {

            MovieInfoTask task = new MovieInfoTask();
            task.execute(id);

            MovieTrailerTask tTask = new MovieTrailerTask();
            tTask.execute(id);

            MovieReviewTask reviewTask = new MovieReviewTask();
            reviewTask.execute(id);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (!mTwoPane) {
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.main_content);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        imgBackdrop = (ImageView) view.findViewById(R.id.backdrop);

        imgPoster = (ImageView) view.findViewById(R.id.posterImage);
        tvRating = (TextView) view.findViewById(R.id.tvRating);
        tvRelease = (TextView) view.findViewById(R.id.tvRelease);
        tvPlot = (TextView) view.findViewById(R.id.tvPlot);

        reviewLayout = (LinearLayout) view.findViewById(R.id.linearUserReview);
        tvUsername = (TextView) view.findViewById(R.id.review_author_name_view);
        tvContent = (TextView) view.findViewById(R.id.review_content_view);
        btnMoreReview = (Button) view.findViewById(R.id.detail_reviews_show_more_button);

        btnMoreReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putParcelableArrayListExtra("reviews", reviewList);
                startActivity(intent);
            }
        });


        trailerLayout = (LinearLayout) view.findViewById(R.id.linearTrailer);
        RecyclerView trailerRecyclerView = (RecyclerView)
                view.findViewById(R.id.recycler_view_trailer);
        trailerRecyclerView.setHasFixedSize(true);
        movieTrailerAdapter = new MovieTrailerAdapter(getActivity(), trailerList);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerRecyclerView.setAdapter(movieTrailerAdapter);

        if (savedInstanceState != null) {
            setMovieInfo(mBackdrop, mPoster, mRating, mReleaseDate, mPlot);
            setMovieReview(reviewList);
        }


        final FloatingActionButton floatingActionButton = (FloatingActionButton)
                view.findViewById(R.id.fab_favorite);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    floatingActionButton.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_heart_white_24dp,
                                    getActivity().getApplicationContext().getTheme()));
                } else {
                    floatingActionButton.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_heart_white_24dp));
                }

                ContentValues movieInfoValues = new ContentValues();
                movieInfoValues.put(MovieContract.MovieEntry._ID, Integer.valueOf(id));
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, mPoster);
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, mBackdrop);
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE_RATE, mRating);
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mReleaseDate);
                movieInfoValues.put(MovieContract.MovieEntry.COLUMN_PLOT, mPlot);
                Log.d(TAG, "onClick: " + MovieContract.MovieEntry.CONTENT_URI);
                getActivity().getContentResolver()
                        .insert(MovieContract.MovieEntry.CONTENT_URI, movieInfoValues);
                Log.d(TAG, "CP added movie info to MDB");


                ContentValues movieTrailerValues = new ContentValues();
                Movie movie;
                for (int i = 0; i < trailerList.size(); i++) {
                    movie = trailerList.get(i);
                    movieTrailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, Integer.valueOf(id));
                    movieTrailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, movie.getKey());
                    movieTrailerValues.put(MovieContract.TrailerEntry.COLUMN_SITE, movie.getSite());
                    getActivity().getContentResolver()
                            .insert(MovieContract.TrailerEntry.CONTENT_URI, movieTrailerValues);
                }
                Log.d(TAG, "CP added trailer to MDB");


                ContentValues movieReviewValues = new ContentValues();
                Review review;
                for (int i = 0; i < reviewList.size(); i++) {
                    review = reviewList.get(i);
                    movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, Integer.valueOf(id));
                    movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
                    movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
                    getActivity().getContentResolver()
                            .insert(MovieContract.ReviewEntry.CONTENT_URI, movieReviewValues);
                }
                Log.d(TAG, "CP added review to MDB");
                Snackbar.make(v, "Mark as favorite", Snackbar.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.key_id), id);
        outState.putString(getString(R.string.key_title), title);
        outState.putString(getString(R.string.key_backdrop), mBackdrop);
        outState.putString(getString(R.string.key_poster), mPoster);
        outState.putString(getString(R.string.key_rating), mRating);
        outState.putString(getString(R.string.key_release_date), mReleaseDate);
        outState.putString(getString(R.string.key_plot), mPlot);
        outState.putParcelableArrayList(getString(R.string.key_trailer), trailerList);
        outState.putParcelableArrayList(getString(R.string.key_review), reviewList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = createShareMovieIntent();
                if (intent != null)
                    startActivity(intent);
                else {
                    Snackbar.make(coordinatorLayout, getString(R.string.sharing_no_video),
                            Snackbar.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent createShareMovieIntent() {
        String link = null;
        if (trailerList.size() != 0) {
            Movie m = trailerList.get(0);
            link = "  Trailer: http://www.youtube.com/watch?v=" + m.getKey();
            Intent intent = ShareCompat.IntentBuilder
                    .from(getActivity())
                    .setText(title + link + MOVIE_SHARE_HASHTAG)
                    .setType("text/plain")
                    .createChooserIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            return intent;
        } else
            return null;
    }

    public void setMovieInfo(String backdrop, String posterPath, String rating,
                             String releaseDate, String plot) {

        Picasso.with(imgBackdrop.getContext())
                .load(POSTER_BASE_URL + BACKDROP_SIZE_PATH + backdrop)
                .into(imgBackdrop);


        Picasso.with(imgPoster.getContext())
                .load(POSTER_BASE_URL + POSTER_SIZE_PATH + posterPath)
                .priority(Picasso.Priority.HIGH).into(imgPoster);


        String ratingValue = getResources()
                .getString(R.string.rating_value, rating);
        tvRating.setText(ratingValue);

        tvRelease.setText(Utility.loadDate(releaseDate));

        tvPlot.setText(plot);
    }

    public void setMovieReview(ArrayList<Review> reviews) {
        if (reviews.size() != 0) {
            Review r = reviews.get(0);
            tvUsername.setText(r.getAuthor());
            tvContent.setText(r.getContent());
        } else {
            reviewLayout.setVisibility(View.GONE);
        }
    }

    private class MovieInfoTask extends FetchMovieInfoTask {

        @Override
        protected void onPostExecute(Movie movieInfo) {
            if (movieInfo != null) {
                mBackdrop = movieInfo.getBackdrop();
                mPoster = movieInfo.getImg();
                mRating = movieInfo.getRating();
                mReleaseDate = movieInfo.getReleaseDate();
                mPlot = movieInfo.getPlot();

                setMovieInfo(mBackdrop, mPoster, mRating, mReleaseDate, mPlot);
            }
        }
    }


    private class MovieTrailerTask extends FetchMovieTrailerTask {

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                trailerList.clear();
                for (Movie m : movies) {
                    trailerList.add(m);
                }
                if (trailerList.size() != 0) {
                    movieTrailerAdapter.notifyDataSetChanged();
                } else {
                    trailerLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private class MovieReviewTask extends FetchMovieReviewTask {

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            if (reviews != null) {
                reviewList.clear();
                for (Review review : reviews) {
                    reviewList.add(review);
                }
                setMovieReview(reviewList);
            }
        }
    }
}
