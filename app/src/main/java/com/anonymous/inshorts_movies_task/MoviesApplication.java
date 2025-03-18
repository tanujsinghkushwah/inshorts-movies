package com.anonymous.inshorts_movies_task;

import android.app.Application;

import com.anonymous.inshorts_movies_task.injection.AppComponent;
import com.anonymous.inshorts_movies_task.injection.DaggerAppComponent;

public class MoviesApplication extends Application {
    
    private AppComponent appComponent;
    
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
} 