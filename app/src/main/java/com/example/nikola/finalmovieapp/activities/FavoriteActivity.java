package com.example.nikola.finalmovieapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nikola.finalmovieapp.fragments.FavoriteDetailFragment;
import com.example.nikola.finalmovieapp.fragments.FavoriteFragment;
import com.example.nikola.finalmovieapp.R;

public class FavoriteActivity extends AppCompatActivity {

    private Boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FavoriteFragment()).commit();

    }

    public void switchToDetail(String id, String title) {
        if (mTwoPane) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, FavoriteDetailFragment.newInstance(id, title, mTwoPane))
                    .commit();
        } else {
            Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
            i.putExtra("class", "FavoriteActivity");
            i.putExtra("id", id);
            i.putExtra("title", title);
            startActivity(i);
        }
    }


}

