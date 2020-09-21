package com.shantanoo.flixster.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shantanoo.flixster.R;
import com.shantanoo.flixster.model.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Shantanoo on 9/19/2020.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Movie> movies;

    private final int MOVIE = 0, POPULAR_MOVIE = 1;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            case POPULAR_MOVIE:
                v = layoutInflater.inflate(R.layout.popular_movie_layout, parent, false);
                viewHolder = new PopularMovieViewHolder(v);
                break;
            case MOVIE:
            default:
                v = layoutInflater.inflate(R.layout.movie_layout, parent, false);
                viewHolder = new MovieViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        String voteAverage = movies.get(position).getVoteAverage();
        if(TextUtils.isEmpty(voteAverage))
            return MOVIE;
        if (Double.parseDouble(voteAverage) >= 7)
            return POPULAR_MOVIE;
        return MOVIE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        switch (holder.getItemViewType()) {
            case POPULAR_MOVIE:
                PopularMovieViewHolder vh1 = (PopularMovieViewHolder) holder;
                vh1.bind(movie);
                break;
            case MOVIE:
            default:
                MovieViewHolder vh2 = (MovieViewHolder) holder;
                vh2.bind(movie);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMoviePoster;
        private TextView tvMovieTitle;
        private TextView tvMovieOverview;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieOverview = itemView.findViewById(R.id.tvMovieOverview);
        }

        public void bind(Movie movie) {
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            tvMovieTitle.setText(movie.getTitle());
            tvMovieOverview.setText(movie.getOverview());
            ivMoviePoster.setContentDescription(movie.getTitle());

            String imagePath = "";
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                imagePath = movie.getPosterPath();
            } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imagePath = movie.getBackdropPath();
            }
            Glide.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .fallback(R.drawable.image_error)
                    //.transition(withCrossFade())
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivMoviePoster);
        }
    }

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPopularMoviePoster;

        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPopularMoviePoster = itemView.findViewById(R.id.ivPopularMoviePoster);
        }

        public void bind(Movie movie) {
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop
            ivPopularMoviePoster.setContentDescription(movie.getTitle());
            Glide.with(context)
                    .load(movie.getBackdropPath())
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .fallback(R.drawable.image_error)
                    //.transition(withCrossFade())
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPopularMoviePoster);
        }
    }
}
