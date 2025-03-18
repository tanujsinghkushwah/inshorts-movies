package com.anonymous.inshorts_movies_task.injection;

import android.app.Application;

import com.anonymous.inshorts_movies_task.MoviesApplication;
import com.anonymous.inshorts_movies_task.ui.bookmarks.BookmarksFragment;
import com.anonymous.inshorts_movies_task.ui.details.MovieDetailsFragment;
import com.anonymous.inshorts_movies_task.ui.home.HomeFragment;
import com.anonymous.inshorts_movies_task.ui.search.SearchFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(HomeFragment fragment);
    void inject(SearchFragment fragment);
    void inject(BookmarksFragment fragment);
    void inject(MovieDetailsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MoviesApplication app);
} 