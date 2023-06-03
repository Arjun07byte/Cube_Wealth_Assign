package com.arjun.cubewealth.repository

import com.arjun.cubewealth.api.RetrofitInstanceMovieDB
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.localDatabase.DatabaseBookmarkMovies

// Main Repository having access to data fetched from both
// the offline and the online modes
class MainRepository(
    private val dbInstance: DatabaseBookmarkMovies
) {
    suspend fun getNowPlayingMovies() = RetrofitInstanceMovieDB.movieDBApiVar.getNowPlayingMovies()
    suspend fun getMovieSynopsis(movieId: Int) =
        RetrofitInstanceMovieDB.movieDBApiVar.getMovieSynopsis(movieId)

    suspend fun getSimilarMovies(movieId: Int) =
        RetrofitInstanceMovieDB.movieDBApiVar.getSimilarMovies(movieId)

    suspend fun getMovieReviews(movieId: Int) =
        RetrofitInstanceMovieDB.movieDBApiVar.getMovieReviews(movieId)

    suspend fun getMovieCredits(movieId: Int) =
        RetrofitInstanceMovieDB.movieDBApiVar.getMovieCredits(movieId)

    suspend fun bookmarkMovie(givenMovie: ItemEachBookmarkMovie) =
        dbInstance.getBookmarkMoviesDAO().bookmarkMovie(givenMovie)

    suspend fun removeBookmarkedMovie(givenMovie: ItemEachBookmarkMovie) =
        dbInstance.getBookmarkMoviesDAO().removeBookmarkedMovie(givenMovie)

    fun getAllBookmarkedMovies() = dbInstance.getBookmarkMoviesDAO().getAllBookmarkedMovies()
}