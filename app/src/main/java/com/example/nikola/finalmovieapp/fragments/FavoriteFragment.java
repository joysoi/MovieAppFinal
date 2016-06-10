package com.example.nikola.finalmovieapp.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.nikola.finalmovieapp.adapters.MovieAdapter;
import com.example.nikola.finalmovieapp.dataBases.MovieContract;
import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    ArrayList<Movie> favoriteList = new ArrayList<>();
    Context mContext;
    String[] projections = {MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_URL};

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.favorite_recycler_view);
        RecyclerView.LayoutManager favLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(favLayoutManager);
        MovieAdapter favoriteAdapter = new MovieAdapter(mContext, favoriteList);
        recyclerView.setAdapter(favoriteAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                projections,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            while (data.moveToNext()) {
                String id = String.valueOf(data.getInt(data.getColumnIndex(MovieContract.MovieEntry._ID)));
                String title = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                String poster = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL));
                Movie m = new Movie(id, poster, title);
                favoriteList.add(m);
            }
            data.close();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
