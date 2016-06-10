package com.example.nikola.finalmovieapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikola.finalmovieapp.adapters.MovieReviewAdapter;
import com.example.nikola.finalmovieapp.parcelable.Review;
import com.example.nikola.finalmovieapp.R;

import java.util.ArrayList;

public class ReviewDialogFragment extends Fragment {

    ArrayList<Review> reviewArrayList = new ArrayList<>();

    public ReviewDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reviewArrayList = getArguments().getParcelableArrayList("review");

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        MovieReviewAdapter reviewAdapter = new MovieReviewAdapter(reviewArrayList);
        mRecyclerView.setAdapter(reviewAdapter);
        return view;
    }

}
