package com.anonymous.inshorts_movies_task.ui.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.inshorts_movies_task.R;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class MovieDetailsFragment extends Fragment {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String DEEP_LINK_PREFIX = "inshortsmovies://movie/";

    private MovieDetailsViewModel viewModel;
    private ImageView ivBackdrop;
    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvRating;
    private TextView tvOverview;
    private MaterialButton btnBookmark;
    private MaterialButton btnShare;
    private Movie currentMovie;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        
        setupViews(view);
        
        int movieId = MovieDetailsFragmentArgs.fromBundle(getArguments()).getMovieId();
        loadMovieDetails(movieId);
    }

    private void setupViews(View view) {
        ivBackdrop = view.findViewById(R.id.iv_backdrop);
        ivPoster = view.findViewById(R.id.iv_poster);
        tvTitle = view.findViewById(R.id.tv_title);
        tvReleaseDate = view.findViewById(R.id.tv_release_date);
        tvRating = view.findViewById(R.id.tv_rating);
        tvOverview = view.findViewById(R.id.tv_overview);
        btnBookmark = view.findViewById(R.id.btn_bookmark);
        btnShare = view.findViewById(R.id.btn_share);
        
        btnBookmark.setOnClickListener(v -> toggleBookmark());
        btnShare.setOnClickListener(v -> shareMovie());
    }

    private void loadMovieDetails(int movieId) {
        viewModel.getMovieById(movieId).observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                currentMovie = movie;
                displayMovieDetails(movie);
                updateBookmarkButton(movie.isBookmarked());
            }
        });
    }

    private void displayMovieDetails(Movie movie) {
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(getString(R.string.release_date, movie.getReleaseDate()));
        tvRating.setText(getString(R.string.rating, movie.getVoteAverage()));
        tvOverview.setText(movie.getOverview());
        
        if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
            Glide.with(this)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(ivPoster);
        }
        
        if (movie.getBackdropPath() != null && !movie.getBackdropPath().isEmpty()) {
            Glide.with(this)
                    .load(IMAGE_BASE_URL + movie.getBackdropPath())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(ivBackdrop);
        }
    }

    private void toggleBookmark() {
        if (currentMovie != null) {
            boolean newBookmarkState = !currentMovie.isBookmarked();
            viewModel.bookmarkMovie(currentMovie, newBookmarkState);
            updateBookmarkButton(newBookmarkState);
        }
    }

    private void updateBookmarkButton(boolean isBookmarked) {
        if (isBookmarked) {
            btnBookmark.setText(R.string.remove_bookmark);
            btnBookmark.setIcon(getResources().getDrawable(R.drawable.ic_bookmarks, null));
        } else {
            btnBookmark.setText(R.string.bookmark);
            btnBookmark.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border, null));
        }
    }

    private void shareMovie() {
        if (currentMovie != null) {
            String shareText = currentMovie.getTitle() + "\n\n" + 
                    currentMovie.getOverview() + "\n\n" +
                    "Check out this movie: " + DEEP_LINK_PREFIX + currentMovie.getId();
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Movie"));
        }
    }
} 