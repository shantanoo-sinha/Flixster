package com.shantanoo.flixster.activity;

import android.app.Activity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.shantanoo.flixster.R;
import com.shantanoo.flixster.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieOverviewActivity extends YouTubeBaseActivity {

    private static final String TAG = "MovieOverviewActivity";

    private static final String YOUTUBE_API_KEY = "AIzaSyAZmCRmYGjb7IaTS2xWo8tiF0Oyzrge65s";
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=6089b53d83c46beab0497f786c89360a";

    private TextView tvMovieTitle;
    private TextView tvMovieOverview;
    private RatingBar ratingBar;
    private YouTubePlayerView youTubePlayerView;

    private boolean isPopular = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);

        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieOverview = findViewById(R.id.tvMovieOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        Movie movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvMovieTitle.setText(movie.getTitle());
        tvMovieOverview.setText(movie.getOverview());

        final float voteAverage = (float) movie.getVoteAverage();
        ratingBar.setRating(voteAverage);
        if(voteAverage >= 5)
            isPopular = true;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0)
                        return;

                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d(TAG, "onSuccess: YouTube Key: " + youtubeKey);
                    initializeYoutube(youtubeKey);
                } catch (JSONException e) {
                    Log.e(TAG, "onSuccess: Failed to parse JSON", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure: Failed to parse data from JSON");
            }
        });
    }

    private void initializeYoutube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        Log.d(TAG, "onInitializationSuccess: YouTube key: " + youtubeKey);
                        // do any work here to cue video, play video, etc.
                        if (isPopular)
                            youTubePlayer.loadVideo(youtubeKey);
                        else
                            youTubePlayer.cueVideo(youtubeKey);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Log.e(TAG, "onInitializationFailure: Failed to initialize Youtube Video");
                        Toast.makeText(getApplicationContext(), "Failed to load YouTube video. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}