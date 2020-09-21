package com.shantanoo.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.shantanoo.flixster.adapter.MovieAdapter;
import com.shantanoo.flixster.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String THE_MOVIE_DB_API_KEY = "api_key=6089b53d83c46beab0497f786c89360a";
    public static final String THE_MOVIE_NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=6089b53d83c46beab0497f786c89360a";

    public static final String TAG = "MainActivity";

    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();
        RecyclerView rvMovies = findViewById(R.id.rvMoviesLayout);
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(THE_MOVIE_NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess:");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray moviesResults = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Result:" + moviesResults.toString());
                    movies.addAll(Movie.fromJsonArray(moviesResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Result Size:" + movies.size());
                    Log.i(TAG, "Result:" + movies.get(0).getPosterPath());
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure:");
            }
        });
    }
}