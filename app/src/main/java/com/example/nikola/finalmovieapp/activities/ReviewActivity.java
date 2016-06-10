package com.example.nikola.finalmovieapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.nikola.finalmovieapp.fragments.ReviewDialogFragment;
import com.example.nikola.finalmovieapp.parcelable.Review;
import com.example.nikola.finalmovieapp.R;

import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Review> reviewArrayList = getIntent().getParcelableArrayListExtra("reviews");

        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("review", reviewArrayList);

        ReviewDialogFragment fragment = new ReviewDialogFragment();
        fragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.reviews_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
