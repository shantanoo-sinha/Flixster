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
import com.shantanoo.flixster.recycleview.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_DB_API_KEY = "api_key=6089b53d83c46beab0497f786c89360a";
    private static final String ENDPOINT = "now_playing";
    private static final String PAGE = "&page=";
    private static final String MOVIE_NOW_PLAYING_URL = MOVIE_DB_BASE_URL + ENDPOINT + "?" + MOVIE_DB_API_KEY;

    private int currentPage = 1;

    private List<Movie> movies;
    private AsyncHttpClient client;
    private MovieAdapter movieAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();
        RecyclerView rvMovies = findViewById(R.id.rvMoviesLayout);
        linearLayoutManager = new LinearLayoutManager(this);
        movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(linearLayoutManager);

        client = new AsyncHttpClient();
        loadNextDataFromApi(currentPage);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(++page);
            }
        };
        rvMovies.addOnScrollListener(scrollListener);

    }

    private void loadNextDataFromApi(int page) {

        String URL = MOVIE_NOW_PLAYING_URL + PAGE + page;
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess: Loading data from the Movie DB");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray moviesResults = jsonObject.getJSONArray(getString(R.string.results));
                    Log.d(TAG, "Result:" + moviesResults.toString());
                    movies.addAll(Movie.fromJsonArray(moviesResults));
                    movieAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Result Size:" + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: Failed to load data from The Movie DB");
            }
        });
    }
}