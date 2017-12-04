package com.example.natan.movietralierapp1;


import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
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
import com.example.natan.movietralierapp1.Adapter.MovieReview;
import com.example.natan.movietralierapp1.Adapter.MovieReviewAdapter;
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

    // for Trailer
    private RecyclerView mRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    //for review
    private RecyclerView mRecyclerViewReview;
    private MovieReviewAdapter mMovieReviewAdapter;

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
        mRecyclerView = findViewById(R.id.recycler_trailer);
        mRecyclerViewReview = findViewById(R.id.recycler_review);
        Stetho.initializeWithDefaults(this);


        ActionBar actionBar = this.getActionBar();
        getActionBar();

        // for trailer adapter
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //for review adapter
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setAutoMeasureEnabled(true);
        mRecyclerViewReview.setLayoutManager(manager);
        mRecyclerViewReview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerViewReview.setItemAnimator(new DefaultItemAnimator());


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


        // for movie trailer
        URL url = NetworkUtils.buildTrailerURl(movie.getId());
        new MovieTrailerAsyncTask().execute(url);

        // for movie review

        URL url1 = NetworkUtils.buildUrlReview(movie.getId());
        new MovieReviewAsyncTask().execute(url1);

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

            mMovieTrailerAdapter = new MovieTrailerAdapter(movies, new MovieTrailerAdapter.ListItemClickListener() {
                @Override
                public void onListItemClick(MovieTrailer movieTrailer) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(NetworkUtils.buildYoutubeUrl(movieTrailer.getTrailer_key()));
                    startActivity(intent);


                }
            });
            mRecyclerView.setAdapter(mMovieTrailerAdapter);

            mMovieTrailerAdapter.notifyDataSetChanged();
        }
    }


    public class MovieReviewAsyncTask extends AsyncTask<URL, Void, List<MovieReview>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MovieReview> doInBackground(URL... urls) {

            List<MovieReview> movieReviews = NetworkUtils.fetchMovieReviewData(urls[0]);
            Log.i("result", String.valueOf(movieReviews));
            return movieReviews;


        }

        @Override
        protected void onPostExecute(List<MovieReview> movieReviews) {

            mMovieReviewAdapter = new MovieReviewAdapter(movieReviews);
            mRecyclerViewReview.setAdapter(mMovieReviewAdapter);
            mMovieReviewAdapter.notifyDataSetChanged();


        }
    }


    @Override
    public void liked(LikeButton likeButton) {

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }
}
