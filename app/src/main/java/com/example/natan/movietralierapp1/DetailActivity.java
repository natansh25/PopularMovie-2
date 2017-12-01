package com.example.natan.movietralierapp1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natan.movietralierapp1.Adapter.Movie;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends Activity implements OnLikeListener {

    private TextView txt_Title;
    private TextView txt_Plot;
    private TextView txt_Rating;
    private TextView txt_Release;
    private ImageView img_Poster;
    private Button btn_save;
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


        getActionBar().setDisplayHomeAsUpEnabled(true);


        Movie movie = getIntent().getParcelableExtra("data");
        txt_Title.setText(movie.getTitle());
        txt_Plot.setText(movie.getOverview());
        txt_Rating.setText(movie.getVoteAverage() + "/10");
        txt_Release.setText(movie.getReleaseDate());
        Picasso.with(img_Poster.getContext()).load("https://image.tmdb.org/t/p/w500" + movie.getImage()).into(img_Poster);

    }



    @Override
    public void liked(LikeButton likeButton) {

        Toast.makeText(this, "welcome pro", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void unLiked(LikeButton likeButton) {

        Toast.makeText(this, "u will be missed", Toast.LENGTH_SHORT).show();
    }
}
