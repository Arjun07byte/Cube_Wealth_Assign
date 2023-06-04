package com.arjun.cubewealth.api

import com.arjun.cubewealth.dataModels.MoviesCreditResponse
import com.arjun.cubewealth.dataModels.MoviesNowPlayingResponse
import com.arjun.cubewealth.dataModels.MoviesReviewResponse
import com.arjun.cubewealth.dataModels.MoviesSimilarResponse
import com.arjun.cubewealth.dataModels.MoviesSynopsisResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// An API interface which is used to fetch details
// from the API using the GET request
interface ApiMovieDB {
    // GET API call to fetch now playing movies
    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key")
        apiKey: String = "3f849d7343479bde4f749c60281a0496",
        @Query("page")
        page: Int
    ): Response<MoviesNowPlayingResponse>

    // GET API call to fetch movie synopsis by movieId
    @GET("{movie_id}")
    suspend fun getMovieSynopsis(
        @Path("movie_id")
        movie_id: Int,
        @Query("api_key")
        apiKey: String = "3f849d7343479bde4f749c60281a0496"
    ): Response<MoviesSynopsisResponse>

    // GET API call to fetch similar movies by movieId
    @GET("{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id")
        movie_id: Int,
        @Query("api_key")
        apiKey: String = "3f849d7343479bde4f749c60281a0496"
    ): Response<MoviesSimilarResponse>

    // GET API call to fetch movie reviews by movieId
    @GET("{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id")
        movie_id: Int,
        @Query("api_key")
        apiKey: String = "3f849d7343479bde4f749c60281a0496"
    ): Response<MoviesReviewResponse>

    // GET API call to fetch movie credits by movieId
    @GET("{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id")
        movie_id: Int,
        @Query("api_key")
        apiKey: String = "3f849d7343479bde4f749c60281a0496"
    ): Response<MoviesCreditResponse>
}






