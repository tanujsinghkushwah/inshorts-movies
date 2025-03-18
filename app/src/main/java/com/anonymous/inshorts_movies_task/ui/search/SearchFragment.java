package com.anonymous.inshorts_movies_task.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class SearchFragment extends Fragment implements MovieAdapter.OnMovieClickListener {

    private static final String ARG_MOVIE_ID = "movieId";
    
    private SearchViewModel viewModel;
    private RecyclerView rvSearchResults;
    private EditText etSearch;
    private TextView tvNoResults;
    private MovieAdapter adapter;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long SEARCH_DELAY_MS = 500;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        
        setupViews(view);
        setupSearch();
        observeViewModel();
    }

    private void setupViews(View view) {
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        etSearch = view.findViewById(R.id.et_search);
        tvNoResults = view.findViewById(R.id.tv_no_results);
        
        adapter = new MovieAdapter(this);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel any pending searches
                searchHandler.removeCallbacks(searchRunnable);
                
                final String query = s.toString().trim();
                if (query.isEmpty()) {
                    adapter.submitList(null);
                    tvNoResults.setVisibility(View.GONE);
                    return;
                }
                
                // Create a new search with delay (debounce)
                searchRunnable = () -> viewModel.setSearchQuery(query);
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void observeViewModel() {
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), movies -> {
            updateUI(movies);
        });
    }

    private void updateUI(List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            rvSearchResults.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            rvSearchResults.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
            adapter.submitList(movies);
        }
    }

    @Override
    public void onMovieClick(Movie movie) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movie.getId());
        Navigation.findNavController(requireView())
                .navigate(R.id.action_search_to_details, args);
    }
} 