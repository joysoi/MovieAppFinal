package com.example.nikola.finalmovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {


    private final Context mContext;
    private ArrayList<Movie> mDataset;

    public MovieTrailerAdapter(Context context, ArrayList<Movie> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.ViewHolder holder, int position) {
        final Movie mMovie = mDataset.get(position);


        final String BASE_URL = "http://img.youtube.com/vi/";
        final String IMG = "/0.jpg";
        final String key = mMovie.getKey();


        if (mMovie.getSite().equals("YouTube")) {
            Picasso.with(mContext)
                    .load(BASE_URL + key + IMG)
                    .priority(Picasso.Priority.LOW)
                    .into(holder.mImageView);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.video_thumbnail_view);
        }
    }
}
