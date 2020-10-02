package com.shantanoo.flixster.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shantanoo.flixster.R;
import com.shantanoo.flixster.activity.MovieOverviewActivity;
import com.shantanoo.flixster.model.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Shantanoo on 9/19/2020.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int MOVIE = 0, POPULAR_MOVIE = 1;
    private Context context;
    private List<Movie> movies;

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
        double voteAverage = movies.get(position).getVoteAverage();
        if (voteAverage >= 5)
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
        private RelativeLayout rlMovieLayout;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.ivMoviePoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvMovieOverview = itemView.findViewById(R.id.tvMovieOverview);
            rlMovieLayout = itemView.findViewById(R.id.movie_layout);
        }

        public void bind(final Movie movie) {
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop

            tvMovieTitle.setText(movie.getTitle());
            tvMovieOverview.setText(movie.getOverview());
            ivMoviePoster.setContentDescription(movie.getTitle());

            rlMovieLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Onclick", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MovieOverviewActivity.class);
                    i.putExtra(context.getString(R.string.movie), Parcels.wrap(movie));

                    Pair<View, String> pair1 = Pair.create((View) tvMovieTitle, context.getString(R.string.movie_title));
                    Pair<View, String> pair2 = Pair.create((View) tvMovieOverview, context.getString(R.string.movie_overview));
                    Pair<View, String> pair3 = Pair.create((View) ivMoviePoster, context.getString(R.string.movie_poster));

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1, pair2, pair3);
                    context.startActivity(i, optionsCompat.toBundle());
                    //context.startActivity(i);
                }
            });

            //int imageWidth = 120;
            String imagePath = "";
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //imageWidth = 120;
                imagePath = movie.getPosterPath();
            } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //imageWidth = 350;
                imagePath = movie.getBackdropPath();
            }
            Glide.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .fallback(R.drawable.image_error)
                    .transition(withCrossFade(500))
                    .transform(new RoundedCornersTransformation(radius, margin))
                    //.override(imageWidth, Target.SIZE_ORIGINAL)
                    //.fitCenter()
                    .into(ivMoviePoster);
        }
    }

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPopularMoviePoster;
        private ImageView ivMoviePlayOverlay;
        private RelativeLayout rlPopularMovieLayout;

        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPopularMoviePoster = itemView.findViewById(R.id.ivPopularMoviePoster);
            ivMoviePlayOverlay = itemView.findViewById(R.id.ivMoviePlayOverlay);
            rlPopularMovieLayout = itemView.findViewById(R.id.popular_movie_layout);
        }

        public void bind(final Movie movie) {
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop
            ivPopularMoviePoster.setContentDescription(movie.getTitle());
            //ivMoviePlayOverlay.setVisibility(View.VISIBLE);

            rlPopularMovieLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Onclick", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, MovieOverviewActivity.class);
                    i.putExtra(context.getString(R.string.movie), Parcels.wrap(movie));

                    Pair<View, String> pair1 = Pair.create((View) ivPopularMoviePoster, context.getString(R.string.movie_poster));

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1);
                    context.startActivity(i, optionsCompat.toBundle());
                    //context.startActivity(i);
                }
            });

            Glide.with(context)
                    .load(movie.getBackdropPath())
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .fallback(R.drawable.image_error)
                    .transition(withCrossFade(500))
                    .transform(new RoundedCornersTransformation(radius, margin))
                    //.override(350, Target.SIZE_ORIGINAL)
                    //.fitCenter()
                    .into(ivPopularMoviePoster);
        }
    }
}
