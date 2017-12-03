package com.example.natan.movietralierapp1.asyncTask;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.natan.movietralierapp1.Adapter.Movie;
import com.example.natan.movietralierapp1.Adapter.MovieTrailer;
import com.example.natan.movietralierapp1.Adapter.MovieTrailerAdapter;
import com.example.natan.movietralierapp1.Network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by natan on 12/2/2017.
 */

public class MovieTrailerAsyncTask extends AsyncTask<URL, Void, List<MovieTrailer>> {

    private RecyclerView mRecyclerView;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    @Override
    protected void onPostExecute(List<MovieTrailer> movies) {
        super.onPostExecute(movies);
    }

    @Override
    protected List<MovieTrailer> doInBackground(URL... urls) {

        List<MovieTrailer> result = NetworkUtils.fetchMovieTrialerData(urls[0]);
        Log.i("result", String.valueOf(result));
        return result;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}


/*
 URL url=NetworkUtils.buildTrailerURl(String.valueOf(440021));
        new MovieTrailerAsyncTask().execute(url);
*/
