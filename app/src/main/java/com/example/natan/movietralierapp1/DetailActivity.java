package com.example.natan.movietralierapp1;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natan.movietralierapp1.Adapter.Movie;
import com.example.natan.movietralierapp1.Data.Contract;
import com.facebook.stetho.Stetho;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements OnLikeListener {

    private TextView txt_Title;
    private TextView txt_Plot;
    private TextView txt_Rating;
    private TextView txt_Release;
    private ImageView img_Poster;

    private LikeButton lykbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        txt_Title = findViewById(R.id.title);
        img_Poster = findViewById(R.id.image_poster);
        txt_Plot = findViewById(R.id.plot);
        txt_Rating = findViewById(R.id.rating);
        txt_Release = findViewById(R.id.release);
        lykbtn = findViewById(R.id.star_button);
        lykbtn.setOnLikeListener(this);
        Stetho.initializeWithDefaults(this);


        ActionBar actionBar = this.getSupportActionBar();
        getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Movie movie = getIntent().getParcelableExtra("data");


        Toast.makeText(this, movie.getId().toString(), Toast.LENGTH_SHORT).show();
        txt_Title.setText(movie.getTitle());
        txt_Plot.setText(movie.getOverview());
        txt_Rating.setText(movie.getVoteAverage() + "/10");
        txt_Release.setText(movie.getReleaseDate());
        Picasso.with(img_Poster.getContext()).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(img_Poster);


        // pressing the button to save the list
        lykbtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                ContentValues contentValues = new ContentValues();

                contentValues.put(Contract.Entry.COLUMN_MOVIE_ID, movie.getId());
                contentValues.put(Contract.Entry.COLUMN_MOVIE_TITLE, movie.getTitle());
                contentValues.put(Contract.Entry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                contentValues.put(Contract.Entry.COLUMN_MOVIE_VOTE, movie.getVoteAverage());
                contentValues.put(Contract.Entry.COLUMN_MOVIE_DATE, movie.getReleaseDate());
                contentValues.put(Contract.Entry.COLUMN_POSTER_PATH, movie.getImage());

                Uri uri = getContentResolver().insert(Contract.Entry.CONTENT_URI, contentValues);
                Toast.makeText(DetailActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

    }

    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }
}
