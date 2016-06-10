package com.example.nikola.finalmovieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikola.finalmovieapp.activities.FavoriteActivity;
import com.example.nikola.finalmovieapp.activities.MainActivity;
import com.example.nikola.finalmovieapp.parcelable.Movie;
import com.example.nikola.finalmovieapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<Movie> mDataset;

    public MovieAdapter(Context context, ArrayList<Movie> myDataset) {
        this.mContext = context;
        this.mDataset = myDataset;
    }

    public void setMoviesData(ArrayList<Movie> moviesData) {
        mDataset = moviesData;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Movie mMovie = mDataset.get(position);

        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
        final String SIZE_PATH = "w185";
        String IMG_PATH = mMovie.getImg();

        Picasso.with(mContext)
                .load(POSTER_BASE_URL + SIZE_PATH + IMG_PATH)
                .error(R.drawable.android_error)
                .into(holder.mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });


        holder.mTextView.setText(mMovie.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MainActivity) {
                    MainActivity main = (MainActivity) mContext;
                    main.switchToDetail(mMovie.getId(), mMovie.getTitle());
                }
                if (mContext instanceof FavoriteActivity) {
                    FavoriteActivity favorite = (FavoriteActivity) mContext;
                    favorite.switchToDetail(mMovie.getId(), mMovie.getTitle());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ImageView mImageView;
        public TextView mTextView;
        public MaterialProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.mImageView = (ImageView) view.findViewById(R.id.imageView);
            this.mTextView = (TextView) view.findViewById(R.id.title);
            this.mProgressBar = (MaterialProgressBar) view.findViewById(R.id.materialProgress);
        }
    }
}