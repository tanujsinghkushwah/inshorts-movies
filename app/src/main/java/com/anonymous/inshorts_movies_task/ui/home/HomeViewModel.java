package com.anonymous.inshorts_movies_task.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import com.anonymous.inshorts_movies_task.MoviesApplication;
import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.anonymous.inshorts_movies_task.data.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeViewModel extends AndroidViewModel {

    @Inject
    MovieRepository repository;
    
    private final CompositeDisposable disposables = new CompositeDisposable();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = ((MoviesApplication) application).getRepository();
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        return LiveDataReactiveStreams.fromPublisher(repository.getTrendingMovies());
    }

    public LiveData<List<Movie>> getNowPlayingMovies() {
        return LiveDataReactiveStreams.fromPublisher(repository.getNowPlayingMovies());
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 