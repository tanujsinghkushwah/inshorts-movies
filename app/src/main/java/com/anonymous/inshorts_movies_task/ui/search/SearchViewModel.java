package com.anonymous.inshorts_movies_task.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.inshorts_movies_task.data.model.Movie;
import com.anonymous.inshorts_movies_task.data.repository.MovieRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchViewModel extends AndroidViewModel {

    @Inject
    MovieRepository repository;
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final PublishSubject<String> searchQuerySubject = PublishSubject.create();
    private final MutableLiveData<List<Movie>> searchResultsLiveData = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        
        disposables.add(
            searchQuerySubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(query -> query.length() >= 2)
                .distinctUntilChanged()
                .switchMap(query -> 
                    repository.searchMovies(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .onErrorReturn(throwable -> {
                            return null;
                        })
                )
                .subscribe(
                    searchResultsLiveData::setValue,
                    throwable -> {
                        searchResultsLiveData.setValue(null);
                    }
                )
        );
    }

    public void setSearchQuery(String query) {
        searchQuerySubject.onNext(query);
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResultsLiveData;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        repository.dispose();
    }
} 