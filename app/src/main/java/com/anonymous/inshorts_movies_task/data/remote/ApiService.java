package com.anonymous.inshorts_movies_task.data.remote;

import com.anonymous.inshorts_movies_task.data.model.MovieResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("trending/movie/{time_window}")
    Observable<MovieResponse> getTrendingMovies(
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey
    );

    @GET("movie/now_playing")
    Observable<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("search/movie")
    Observable<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Observable<MovieResponse.MovieDto> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
} 