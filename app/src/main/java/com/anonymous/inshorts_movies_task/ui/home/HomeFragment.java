package com.anonymous.inshorts_movies_task.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.inshorts_movies_task.R;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.anonymous.inshorts_movies_task.ui.adapter.MovieAdapter;

public class HomeFragment extends Fragment implements MovieAdapter.OnMovieClickListener {
    
    private static final String TAG = "HomeFragment";
    private static final String ARG_MOVIE_ID = "movieId";
    
    private HomeViewModel viewModel;
    private RecyclerView rvTrendingMovies;
    private RecyclerView rvNowPlayingMovies;
    private MovieAdapter trendingAdapter;
    private MovieAdapter nowPlayingAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        setupRecyclerViews(view);
        observeViewModel();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume - fragment is now visible to the user");
    }

    private void setupRecyclerViews(View view) {
        rvTrendingMovies = view.findViewById(R.id.rv_trending_movies);
        rvNowPlayingMovies = view.findViewById(R.id.rv_now_playing_movies);
        
        trendingAdapter = new MovieAdapter(this);
        nowPlayingAdapter = new MovieAdapter(this);
        
        rvTrendingMovies.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvNowPlayingMovies.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        rvTrendingMovies.setAdapter(trendingAdapter);
        rvNowPlayingMovies.setAdapter(nowPlayingAdapter);
    }

    private void observeViewModel() {
        viewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movies -> {
            Log.d(TAG, "Received trending movies update: " + (movies != null ? movies.size() : 0) + " movies");
            trendingAdapter.submitList(movies);
        });
        
        viewModel.getNowPlayingMovies().observe(getViewLifecycleOwner(), movies -> {
            Log.d(TAG, "Received now playing movies update: " + (movies != null ? movies.size() : 0) + " movies");
            nowPlayingAdapter.submitList(movies);
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        Log.d(TAG, "Clicked on movie: " + movie.getTitle() + ", ID: " + movie.getId() + ", Bookmarked: " + movie.isBookmarked());
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movie.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_home_to_details, args);
    }
} 