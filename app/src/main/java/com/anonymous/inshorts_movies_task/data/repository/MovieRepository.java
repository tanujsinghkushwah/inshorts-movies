package com.anonymous.inshorts_movies_task.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.inshorts_movies_task.R;
import com.anonymous.inshorts_movies_task.data.local.AppDatabase;
import com.anonymous.inshorts_movies_task.data.local.MovieDao;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.anonymous.inshorts_movies_task.data.model.MovieResponse;
import com.anonymous.inshorts_movies_task.data.remote.ApiClient;
import com.anonymous.inshorts_movies_task.data.remote.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class MovieRepository {
    private static final String TAG = "MovieRepository";
    
    private final ApiService apiService;
    private final MovieDao movieDao;
    private final CompositeDisposable disposables;
    private final String apiKey;

    @Inject
    public MovieRepository(ApiService apiService, MovieDao movieDao, Application application) {
        this.apiService = apiService;
        this.movieDao = movieDao;
        this.disposables = new CompositeDisposable();
        this.apiKey = application.getString(R.string.tmdb_api_key);
    }

    public MovieRepository(Application application) {
        this.apiService = ApiClient.getClient().create(ApiService.class);
        AppDatabase database = AppDatabase.getInstance(application);
        this.movieDao = database.movieDao();
        this.disposables = new CompositeDisposable();
        this.apiKey = application.getString(R.string.tmdb_api_key);
    }

    public Flowable<List<Movie>> getTrendingMovies() {
        refreshTrendingMovies();
        return movieDao.getMoviesByCategory("trending");
    }

    public Flowable<List<Movie>> getNowPlayingMovies() {
        refreshNowPlayingMovies();
        return movieDao.getMoviesByCategory("now_playing");
    }

    public Flowable<List<Movie>> getBookmarkedMovies() {
        return movieDao.getBookmarkedMovies();
    }

    public Flowable<Movie> getMovieById(int movieId) {
        return movieDao.getMovieById(movieId);
    }

    public void bookmarkMovie(Movie movie, boolean isBookmarked) {
        movie.setBookmarked(isBookmarked);
        
        try {
            movieDao.updateMovie(movie)
                .subscribeOn(Schedulers.io())
                .blockingAwait();
            Log.d(TAG, "Movie bookmark status updated successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error updating movie bookmark status", e);
        }
    }

    public Flowable<List<Movie>> searchMovies(String query) {
        refreshSearchResults(query);
        return movieDao.searchMovies(query);
    }

    private void refreshTrendingMovies() {
        disposables.add(
            apiService.getTrendingMovies("day", apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieResponse.MovieDto movieDto : response.getResults()) {
                        Movie newMovie = movieDto.toMovie("trending");
                        checkAndApplyBookmarkStatus(newMovie);
                        movies.add(newMovie);
                    }
                    return movies;
                })
                .flatMapCompletable(movieDao::insertMovies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> Log.d(TAG, "Trending movies refreshed successfully"),
                    throwable -> Log.e(TAG, "Failed to refresh trending movies", throwable)
                )
        );
    }

    private void refreshNowPlayingMovies() {
        disposables.add(
            apiService.getNowPlayingMovies(apiKey, "en-US", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieResponse.MovieDto movieDto : response.getResults()) {
                        Movie newMovie = movieDto.toMovie("now_playing");
                        checkAndApplyBookmarkStatus(newMovie);
                        movies.add(newMovie);
                    }
                    return movies;
                })
                .flatMapCompletable(movieDao::insertMovies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> Log.d(TAG, "Now playing movies refreshed successfully"),
                    throwable -> Log.e(TAG, "Failed to refresh now playing movies", throwable)
                )
        );
    }

    private void refreshSearchResults(String query) {
        disposables.add(
            apiService.searchMovies(apiKey, query, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(response -> {
                    List<Movie> movies = new ArrayList<>();
                    for (MovieResponse.MovieDto movieDto : response.getResults()) {
                        Movie newMovie = movieDto.toMovie("search");
                        checkAndApplyBookmarkStatus(newMovie);
                        movies.add(newMovie);
                    }
                    return movies;
                })
                .flatMapCompletable(movieDao::insertMovies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> Log.d(TAG, "Search results refreshed successfully"),
                    throwable -> Log.e(TAG, "Failed to refresh search results", throwable)
                )
        );
    }

    private void checkAndApplyBookmarkStatus(Movie newMovie) {
        try {
            List<Movie> existingMovies = movieDao.getMovieByIdSync(newMovie.getId())
                .subscribeOn(Schedulers.io())
                .blockingGet();
            
            if (existingMovies != null && !existingMovies.isEmpty()) {
                Movie existingMovie = existingMovies.get(0);
                newMovie.setBookmarked(existingMovie.isBookmarked());
                Log.d(TAG, "Applied bookmark status: " + existingMovie.isBookmarked() + 
                      " to movie: " + newMovie.getTitle());
            }
        } catch (Exception e) {
            Log.d(TAG, "No existing bookmark status found for movie: " + newMovie.getTitle());
        }
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
} 