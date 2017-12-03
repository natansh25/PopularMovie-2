package com.example.natan.movietralierapp1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.natan.movietralierapp1.Adapter.FavoritesAdapter;
import com.example.natan.movietralierapp1.Adapter.Movie;
import com.example.natan.movietralierapp1.Adapter.MovieTrailer;
import com.example.natan.movietralierapp1.Adapter.RecyclerMovie;
import com.example.natan.movietralierapp1.Data.Contract;
import com.example.natan.movietralierapp1.Network.NetworkUtils;
import com.example.natan.movietralierapp1.asyncTask.MovieTrailerAsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private RecyclerMovie mRecyclerMovie;
    private RecyclerView mrecyclerView;
    private ProgressBar mProgressBar;
    private static final int MOVIE_LOADER_ID = 1;
    private FavoritesAdapter mFavoritesAdapter;


    // onSaveinstance varibale

    private final static String MENU_SELECTED = "selected";
    private int selected = -1;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mrecyclerView = findViewById(R.id.recyclerView);
        mProgressBar = findViewById(R.id.progress_bar);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        // mFavoritesAdapter=new FavoritesAdapter(this);
        mrecyclerView.setLayoutManager(mLayoutManager);
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());
        build("popularity.desc");

        //onSavedInstance loading if exist

        if (savedInstanceState != null) {
            selected = savedInstanceState.getInt(MENU_SELECTED);

            if (selected == -1) {
                build("popularity.desc");
            } else if (selected == R.id.highest_Rated) {
                build("vote_count.desc");
            } else {
                build("popularity.desc");
            }

        }
    }


    // for background query
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {


            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }


            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(Contract.Entry.CONTENT_URI,
                            null,
                            null,
                            null,
                            Contract.Entry.COLUMN_MOVIE_ID);

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
                Log.i("tag", String.valueOf(data));
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mFavoritesAdapter.swapCursor(data);


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mFavoritesAdapter.swapCursor(null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        //getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID , null, this);
    }


    //Creating inner class for Async Task

    public class MovieDbQUeryTask extends AsyncTask<URL, Void, List<Movie>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<Movie> doInBackground(URL... urls) {

            List<Movie> result = NetworkUtils.fetchMovieData(urls[0]);
            return result;
        }


        @Override
        protected void onPostExecute(List<Movie> movies) {


            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerMovie = new RecyclerMovie(MainActivity.this, movies, new RecyclerMovie.ListItemClickListener() {
                @Override
                public void onListItemClick(Movie movie) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("data", movie);
                    startActivity(intent);

                }
            });

            mrecyclerView.setAdapter(mRecyclerMovie);
            mRecyclerMovie.notifyDataSetChanged();

        }
    }

    //onsaveInstanceState

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MENU_SELECTED, selected);
        super.onSaveInstanceState(outState);
    }


    // For menu settings

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.highest_Rated:
                build("vote_count.desc");
                selected = id;

                break;

            case R.id.most_popular:
                build("popularity.desc");
                selected = id;
                break;
            case R.id.favorites:

                // android.content.Loader<Object> loader= getLoaderManager().getLoader(MOVIE_LOADER_ID);
                //  if (loader == null) {
                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                mFavoritesAdapter = new FavoritesAdapter(new RecyclerMovie.ListItemClickListener() {
                    @Override
                    public void onListItemClick(Movie movie) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("data", movie);
                        startActivity(intent);
                    }
                }, this);
                mrecyclerView.setAdapter(mFavoritesAdapter);
                // } else {
                //     loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, this);
                //  }

                //as  break;
        }

        return super.onOptionsItemSelected(item);
    }

    private URL build(String sort) {
        URL final_Url = NetworkUtils.buildURl(sort);
        new MovieDbQUeryTask().execute(final_Url);
        return final_Url;
    }
}
