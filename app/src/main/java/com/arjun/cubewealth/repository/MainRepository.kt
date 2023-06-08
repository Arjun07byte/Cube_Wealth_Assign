package com.arjun.cubewealth.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.arjun.cubewealth.api.RetrofitInstanceMovieDB
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.localDatabase.DatabaseBookmarkMovies
import com.arjun.cubewealth.paging.NowPlayingPagingSource

// Main Repository having access to data fetched from both
// the offline and the online modes
class MainRepository(
    private val dbInstance: DatabaseBookmarkMovies
) {
    fun getNowPlayingMovies() = Pager(
        config = PagingConfig(20, 40)
    ) {
        NowPlayingPagingSource(RetrofitInstanceMovieDB.movieDBApiVar)
    }.liveData

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

    fun getAllBookmarkedMovieIds() = dbInstance.getBookmarkMoviesDAO().getAllBookmarkedMovieIds()
}