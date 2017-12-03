package com.example.natan.movietralierapp1;


import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natan.movietralierapp1.Adapter.Movie;
import com.example.natan.movietralierapp1.Adapter.MovieTrailer;
import com.example.natan.movietralierapp1.Adapter.MovieTrailerAdapter;
import com.example.natan.movietralierapp1.Data.Contract;
import com.example.natan.movietralierapp1.Network.NetworkUtils;
import com.facebook.stetho.Stetho;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailActivity extends Activity implements OnLikeListener {

    private TextView txt_Title;
    private TextView txt_Plot;
    private TextView txt_Rating;
    private TextView txt_Release;
    private ImageView img_Poster;

    private LikeButton lykbtn;


    private RecyclerView mRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_2);
        txt_Title = findViewById(R.id.title);
        img_Poster = findViewById(R.id.image_poster);
        txt_Plot = findViewById(R.id.plot);
        txt_Rating = findViewById(R.id.rating);
        txt_Release = findViewById(R.id.release);
        lykbtn = findViewById(R.id.star_button);
        lykbtn.setOnLikeListener(this);
        mRecyclerView = findViewById(R.id.recycler_trailer);
        Stetho.initializeWithDefaults(this);


        ActionBar actionBar = this.getActionBar();
        getActionBar();


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

        URL url = NetworkUtils.buildTrailerURl(movie.getId());
        new MovieTrailerAsyncTask().execute(url);

    }


    //Async task for Movie Trailer-----------------------------------------------

    public class MovieTrailerAsyncTask extends AsyncTask<URL, Void, List<MovieTrailer>> {


        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }


        @Override
        protected List<MovieTrailer> doInBackground(URL... urls) {

            List<MovieTrailer> result = NetworkUtils.fetchMovieTrialerData(urls[0]);
            Log.i("result", String.valueOf(result));
            return result;

        }


        @Override
        protected void onPostExecute(List<MovieTrailer> movies) {

            mMovieTrailerAdapter = new MovieTrailerAdapter(movies);
            mRecyclerView.setAdapter(mMovieTrailerAdapter);

            mMovieTrailerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }
}
