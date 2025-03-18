package com.anonymous.inshorts_movies_task.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.anonymous.inshorts_movies_task.MoviesApplication;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.anonymous.inshorts_movies_task.data.repository.MovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MovieDetailsViewModel extends AndroidViewModel {

    @Inject
    MovieRepository repository;
    
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = ((MoviesApplication) application).getRepository();
    }

    public LiveData<Movie> getMovieById(int movieId) {
        return LiveDataReactiveStreams.fromPublisher(repository.getMovieById(movieId));
    }

    public void bookmarkMovie(Movie movie, boolean isBookmarked) {
        repository.bookmarkMovie(movie, isBookmarked);
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 