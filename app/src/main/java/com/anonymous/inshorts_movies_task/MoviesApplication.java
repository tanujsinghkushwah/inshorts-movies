package com.anonymous.inshorts_movies_task;

import android.app.Application;

import com.anonymous.inshorts_movies_task.data.repository.MovieRepository;
import com.anonymous.inshorts_movies_task.injection.AppComponent;
import com.anonymous.inshorts_movies_task.injection.DaggerAppComponent;

import javax.inject.Inject;

public class MoviesApplication extends Application {
    
    private AppComponent appComponent;
    
    @Inject
    MovieRepository repository;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        
        appComponent.inject(this);
    }
    
    public AppComponent getAppComponent() {
        return appComponent;
    }
    
    public MovieRepository getRepository() {
        return repository;
    }
} 