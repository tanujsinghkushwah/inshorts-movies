package com.anonymous.inshorts_movies_task.injection;

import android.app.Application;

import com.anonymous.inshorts_movies_task.data.local.AppDatabase;
import com.anonymous.inshorts_movies_task.data.local.MovieDao;
import com.anonymous.inshorts_movies_task.data.remote.ApiClient;
import com.anonymous.inshorts_movies_task.data.remote.ApiService;
import com.anonymous.inshorts_movies_task.data.repository.MovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application) {
        return AppDatabase.getInstance(application);
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(AppDatabase database) {
        return database.movieDao();
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        return ApiClient.getClient().create(ApiService.class);
    }

    @Provides
    @Singleton
    MovieRepository provideMovieRepository(ApiService apiService, MovieDao movieDao, Application application) {
        return new MovieRepository(apiService, movieDao, application);
    }
} 