package com.arjun.cubewealth.repository

import com.arjun.cubewealth.api.MovieDBRetrofitInstance

// Main Repository having access to data fetched from both
// the offline and the online modes
class MainRepository {
    suspend fun getNowPlayingMovies() = MovieDBRetrofitInstance.movieDBApiVar.getNowPlayingMovies()
    suspend fun getMovieSynopsis(movieId: Int) =
        MovieDBRetrofitInstance.movieDBApiVar.getMovieSynopsis(movieId)

    suspend fun getSimilarMovies(movieId: Int) =
        MovieDBRetrofitInstance.movieDBApiVar.getSimilarMovies(movieId)

    suspend fun getMovieReviews(movieId: Int) =
        MovieDBRetrofitInstance.movieDBApiVar.getMovieReviews(movieId)

    suspend fun getMovieCredits(movieId: Int) =
        MovieDBRetrofitInstance.movieDBApiVar.getMovieCredits(movieId)
}