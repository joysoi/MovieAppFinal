package com.example.nikola.finalmovieapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.nikola.finalmovieapp.parcelable.Review;
import com.example.nikola.finalmovieapp.R;

import java.util.ArrayList;


public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    ArrayList<Review> mDataset = new ArrayList<>();

    public MovieReviewAdapter(ArrayList<Review> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MovieReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review r = mDataset.get(position);
        holder.tvAuthor.setText(r.getAuthor());
        holder.tvContent.setMaxLines(Integer.MAX_VALUE);
        holder.tvContent.setText(r.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthor;
        public TextView tvContent;

        public ViewHolder(View view) {
            super(view);
            tvAuthor = (TextView) view.findViewById(R.id.review_author_name_view);
            tvContent = (TextView) view.findViewById(R.id.review_content_view);
        }
    }
}
