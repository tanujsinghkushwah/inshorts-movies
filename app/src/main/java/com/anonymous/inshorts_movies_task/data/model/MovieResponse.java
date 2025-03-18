package com.anonymous.inshorts_movies_task.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    private List<MovieDto> results;

    public List<MovieDto> getResults() {
        return results;
    }

    public static class MovieDto {
        @SerializedName("id")
        private int id;
        
        @SerializedName("title")
        private String title;
        
        @SerializedName("overview")
        private String overview;
        
        @SerializedName("poster_path")
        private String posterPath;
        
        @SerializedName("backdrop_path")
        private String backdropPath;
        
        @SerializedName("release_date")
        private String releaseDate;
        
        @SerializedName("vote_average")
        private float voteAverage;

        public Movie toMovie(String category) {
            return new Movie(
                    id,
                    title,
                    overview,
                    posterPath,
                    backdropPath,
                    releaseDate,
                    voteAverage,
                    category,
                    false
            );
        }
    }
} 