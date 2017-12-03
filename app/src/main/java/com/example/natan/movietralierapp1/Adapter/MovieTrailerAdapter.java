package com.example.natan.movietralierapp1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.natan.movietralierapp1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by natan on 12/2/2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MyViewHolder> {

    private List<MovieTrailer> mMovieTrailerList;
    private Context context;

    public MovieTrailerAdapter(List<MovieTrailer> movieTrailerList) {
        mMovieTrailerList = movieTrailerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_trailer, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieTrailer movieTrailer = mMovieTrailerList.get(position);

        Picasso.with(context)
                .load("http://img.youtube.com/vi/" + movieTrailer.getTrailer_key() +"/0.jpg")
                .into(holder.img);




        Log.i("picasso","http://img.youtube.com/vi/" + movieTrailer.getTrailer_key()+"/0.jpg");

    }

    @Override
    public int getItemCount() {
        return mMovieTrailerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_View_trailer);
        }
    }

}
