package com.example.nikola.finalmovieapp.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.parcelable.Review;
import com.example.nikola.finalmovieapp.R;
import com.example.nikola.finalmovieapp.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER_ID = 0;
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;

    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMoviesApp";
    private static final String TAG = "FavoriteDetailFragment";
    private final String POSTER_SIZE_PATH = "w342";
    private final String BACKDROP_SIZE_PATH = "w780";

    CoordinatorLayout coordinatorLayout;
    ImageView imgPoster, imgBackdrop;
    TextView tvRating, tvRelease, tvPlot, tvUsername, tvContent;
    Button btnMoreReview;
    LinearLayout reviewLayout, trailerLayout;
    MovieTrailerAdapter movieTrailerAdapter;

    private Boolean mTwoPane = false;
    private String id, title;
    private String mBackdrop, mPoster, mRating, mReleaseDate, mPlot;
    private ArrayList<Movie> trailerList = new ArrayList<>();
    private ArrayList<Review> reviewList = new ArrayList<>();

    public static FavoriteDetailFragment newInstance(String id, String title, Boolean twoPane) {
        FavoriteDetailFragment f = new FavoriteDetailFragment();


        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("title", title);
        args.putBoolean("twoPane", twoPane);
        f.setArguments(args);

        return f;
    }

    public FavoriteDetailFragment() {

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
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            floatingActionButton.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_heart_white_24dp,
                            getActivity().getApplicationContext().getTheme()));
        } else {
            floatingActionButton.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_heart_white_24dp));
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    floatingActionButton.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_heart_outline_white_24dp,
                                    getActivity().getApplicationContext().getTheme()));
                } else {
                    floatingActionButton.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_heart_outline_white_24dp));
                }
                ContentResolver cr = getActivity().getContentResolver();
                int movieRow = cr.delete(MovieContract.MovieEntry.CONTENT_URI,
                        MovieContract.MovieEntry._ID + "=?", new String[]{id});
                Log.d(TAG, "Number of movies deleted: " + movieRow);
                int trailerRow = cr.delete(MovieContract.TrailerEntry.CONTENT_URI,
                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                Log.d(TAG, "Number of trailer deleted: " + trailerRow);
                int reviewRow = cr.delete(MovieContract.ReviewEntry.CONTENT_URI,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "=?", new String[]{id});
                Log.d(TAG, "Number of review deleted: " + reviewRow);
                Snackbar.make(v, "Removed from favorites", Snackbar.LENGTH_LONG).show();
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

    @Override
    public Loader<Cursor> onCreateLoader(int idLoader, Bundle args) {
        CursorLoader loader = null;
        switch (idLoader) {
            case MOVIE_LOADER_ID:
                loader = new CursorLoader(getActivity(),
                        MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{id},
                        null);
                break;
            case TRAILER_LOADER_ID:
                loader = new CursorLoader(getActivity(),
                        MovieContract.TrailerEntry.CONTENT_URI,
                        null,
                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{id},
                        null);
                break;
            case REVIEW_LOADER_ID:
                loader = new CursorLoader(getActivity(),
                        MovieContract.ReviewEntry.CONTENT_URI,
                        null,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{id},
                        null);
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                if (data.getCount() == 0) {
                    Log.d(TAG, "No data in DB");
                }
                if (data != null) {
                    Log.v(TAG, DatabaseUtils.dumpCursorToString(data));
                    if (data.moveToFirst()) {
                        mBackdrop = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_URL));
                        mPoster = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL));
                        mRating = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVERAGE_RATE));
                        mReleaseDate = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                        mPlot = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_PLOT));
                    }
                    setMovieInfo(mBackdrop, mPoster, mRating, mReleaseDate, mPlot);
                }
                break;
            case TRAILER_LOADER_ID:
                if (data.getCount() == 0) {
                    Log.d(TAG, "No data in DB");
                }
                if (data != null) {
                    Log.v(TAG, DatabaseUtils.dumpCursorToString(data));
                    if (data.moveToFirst()) {
                        do {
                            String key = data.getString(data.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY));
                            String site = data.getString(data.getColumnIndex(MovieContract.TrailerEntry.COLUMN_SITE));
                            Movie m = new Movie(key, site);
                            trailerList.add(m);
                        } while (data.moveToNext());
                    }
                    if (trailerList.size() != 0) {
                        movieTrailerAdapter.notifyDataSetChanged();
                    } else {
                        trailerLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case REVIEW_LOADER_ID:
                if (data.getCount() == 0) {
                    Log.d(TAG, "No data in DB");
                }
                if (data != null) {
                    Log.v(TAG, DatabaseUtils.dumpCursorToString(data));
                    if (data.moveToFirst()) {
                        do {
                            String author = data.getString(data.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
                            String content = data.getString(data.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT));
                            Review review = new Review(author, content);
                            reviewList.add(review);
                        } while (data.moveToNext());
                    }
                    setMovieReview(reviewList);
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
