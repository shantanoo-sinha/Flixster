package com.shantanoo.flixster.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shantanoo on 9/19/2020.
 */
public class Movie {
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String voteAverage;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        String formattedPath = String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
        return formattedPath;
    }

    public String getBackdropPath() {
        if(TextUtils.isEmpty(backdropPath) || backdropPath.equals("null")) {
            return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
        }
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropPath);
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getString("vote_average");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException{
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++)
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        return movies;
    }
}