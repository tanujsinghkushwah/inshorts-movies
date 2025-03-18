package com.anonymous.inshorts_movies_task.ui.bookmarks;

import android.os.Bundle;
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

public class BookmarksFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private static final String ARG_MOVIE_ID = "movieId";
    
    private BookmarksViewModel viewModel;
    private RecyclerView rvBookmarkedMovies;
    private TextView tvNoBookmarks;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(BookmarksViewModel.class);
        
        setupViews(view);
        observeViewModel();
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
            adapter.submitList(movies);
            
            if (movies == null || movies.isEmpty()) {
                tvNoBookmarks.setVisibility(View.VISIBLE);
                rvBookmarkedMovies.setVisibility(View.GONE);
            } else {
                tvNoBookmarks.setVisibility(View.GONE);
                rvBookmarkedMovies.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movie.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_bookmarks_to_details, args);
    }
} 