package com.anonymous.inshorts_movies_task.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.inshorts_movies_task.R;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.bumptech.glide.Glide;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(OnMovieClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getOverview().equals(newItem.getOverview()) &&
                    oldItem.isBookmarked() == newItem.isBookmarked();
        }
    };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.bind(movie, listener);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMoviePoster;
        private final TextView tvMovieTitle;
        private final TextView tvMovieOverview;
        private final TextView tvMovieRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvMovieOverview = itemView.findViewById(R.id.tv_movie_overview);
            tvMovieRating = itemView.findViewById(R.id.tv_movie_rating);
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            tvMovieTitle.setText(movie.getTitle());
            tvMovieOverview.setText(movie.getOverview());
            tvMovieRating.setText(String.format("Rating: %.1f/10", movie.getVoteAverage()));

            if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(IMAGE_BASE_URL + movie.getPosterPath())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_broken_image)
                        .into(ivMoviePoster);
            } else {
                ivMoviePoster.setImageResource(R.drawable.ic_image_placeholder);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
} 