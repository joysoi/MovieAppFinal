package com.example.nikola.finalmovieapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikola.finalmovieapp.adapters.MovieAdapter;
import com.example.nikola.finalmovieapp.movieData.FetchPopTopMovieTask;
import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.R;

import java.util.ArrayList;

public class RatingMoviesFragment extends Fragment {

    private static final String RATING_LIST = "ratingList";
    MovieAdapter ratingAdapter;
    ArrayList<Movie> mRatingList = new ArrayList<>();
    private String sortOrder;
    private Context mContext;

    public RatingMoviesFragment() {
    }

    public static RatingMoviesFragment newInstance(String sort) {
        RatingMoviesFragment fragment = new RatingMoviesFragment();
        Bundle args = new Bundle();
        args.putString("sort", sort);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortOrder = getArguments().getString("sort");
        mContext = getActivity();

        if (savedInstanceState != null) {
            mRatingList = savedInstanceState.getParcelableArrayList(RATING_LIST);
        } else {
            FetchRatingMovies rm = new FetchRatingMovies();
            rm.execute(sortOrder);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_movies, container, false);

        ratingAdapter = new MovieAdapter(mContext, mRatingList);

        RecyclerView ratRecyclerView = (RecyclerView) view.findViewById(R.id.rat_recycler_view);
        RecyclerView.LayoutManager ratLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        ratRecyclerView.setLayoutManager(ratLayoutManager);
        ratRecyclerView.setAdapter(ratingAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RATING_LIST, mRatingList);
        super.onSaveInstanceState(outState);
    }

    public class FetchRatingMovies extends FetchPopTopMovieTask {

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

            if (movies != null) {
                mRatingList.clear();
                for (Movie m : movies) {
                    mRatingList.add(m);
                }
                ratingAdapter.setMoviesData(mRatingList);
                ratingAdapter.notifyDataSetChanged();
            }
        }
    }

}
