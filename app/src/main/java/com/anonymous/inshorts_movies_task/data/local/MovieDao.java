package com.anonymous.inshorts_movies_task.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.inshorts_movies_task.data.model.Movie;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMovies(List<Movie> movies);

    @Update
    Completable updateMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE category = :category")
    Flowable<List<Movie>> getMoviesByCategory(String category);

    @Query("SELECT * FROM movies WHERE isBookmarked = 1")
    Flowable<List<Movie>> getBookmarkedMovies();

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Flowable<Movie> getMovieById(int movieId);

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Single<List<Movie>> getMovieByIdSync(int movieId);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    Flowable<List<Movie>> searchMovies(String query);
} 