package com.anonymous.inshorts_movies_task.ui.bookmarks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.List;

public class BookmarksFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private static final String TAG = "BookmarksFragment";
    private static final String ARG_MOVIE_ID = "movieId";
    
    private BookmarksViewModel viewModel;
    private RecyclerView rvBookmarkedMovies;
    private TextView tvNoBookmarks;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        
        viewModel = new ViewModelProvider(this).get(BookmarksViewModel.class);
        
        setupViews(view);
        observeViewModel();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume - fragment is now visible to the user");
    }

    private void setupViews(View view) {
        rvBookmarkedMovies = view.findViewById(R.id.rv_bookmarked_movies);
        tvNoBookmarks = view.findViewById(R.id.tv_no_bookmarks);
        
        adapter = new MovieAdapter(this);
        rvBookmarkedMovies.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvBookmarkedMovies.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getBookmarkedMovies().observe(getViewLifecycleOwner(), movies -> {
            logBookmarkedMovies(movies);
            adapter.submitList(movies);
            
            if (movies == null || movies.isEmpty()) {
                tvNoBookmarks.setVisibility(View.VISIBLE);
                rvBookmarkedMovies.setVisibility(View.GONE);
                Log.d(TAG, "No bookmarked movies to display");
            } else {
                tvNoBookmarks.setVisibility(View.GONE);
                rvBookmarkedMovies.setVisibility(View.VISIBLE);
                Log.d(TAG, "Displaying " + movies.size() + " bookmarked movies");
            }
        });
    }
    
    private void logBookmarkedMovies(List<Movie> movies) {
        if (movies == null) {
            Log.d(TAG, "Bookmarked movies list is null");
            return;
        }
        
        if (movies.isEmpty()) {
            Log.d(TAG, "Bookmarked movies list is empty");
            return;
        }
        
        Log.d(TAG, "Bookmarked movies: " + movies.size());
        for (Movie movie : movies) {
            Log.d(TAG, "Bookmarked movie: " + movie.getTitle() + ", ID: " + movie.getId());
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movie.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_bookmarks_to_details, args);
    }
} 