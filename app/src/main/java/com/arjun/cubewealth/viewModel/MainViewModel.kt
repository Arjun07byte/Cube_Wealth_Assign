package com.arjun.cubewealth.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.cubewealth.dataModels.DisplayMovieCredits
import com.arjun.cubewealth.dataModels.DisplayMovieReview
import com.arjun.cubewealth.dataModels.DisplaySimilarMovie
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.dataModels.ItemImageWithLabelDisplay
import com.arjun.cubewealth.dataModels.ItemMovieReviewDisplay
import com.arjun.cubewealth.dataModels.MoviesCreditResponse
import com.arjun.cubewealth.dataModels.MoviesNowPlayingResponse
import com.arjun.cubewealth.dataModels.MoviesReviewResponse
import com.arjun.cubewealth.dataModels.MoviesSimilarResponse
import com.arjun.cubewealth.dataModels.MoviesSynopsisResponse
import com.arjun.cubewealth.repository.MainRepository
import com.arjun.cubewealth.utills.APIResponseStateClass
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryInstance: MainRepository
) : ViewModel() {
    val liveDataNowPlayingMovieList: MutableLiveData<APIResponseStateClass<MoviesNowPlayingResponse>> =
        MutableLiveData()
    val liveDataMoviesCredits: MutableLiveData<APIResponseStateClass<DisplayMovieCredits>> =
        MutableLiveData()
    val liveDataSimilarMoviesList: MutableLiveData<APIResponseStateClass<DisplaySimilarMovie>> =
        MutableLiveData()
    val liveDataMovieSynopsisList: MutableLiveData<APIResponseStateClass<MoviesSynopsisResponse>> =
        MutableLiveData()
    val liveDataMovieReviewsList: MutableLiveData<APIResponseStateClass<DisplayMovieReview>> =
        MutableLiveData()
    var listBookmarkedMoviesIds: List<Int> = listOf()

    fun getNowPlayingMovies(pageIdx: Int) {
        viewModelScope.launch {
            val response = repositoryInstance.getNowPlayingMovies(pageIdx)
            liveDataNowPlayingMovieList.postValue(
                APIResponseStateClass.LoadingResponseClass()
            )
            if (response.isSuccessful) {
                if (response.body() != null) liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.SuccessResponseClass(response.body()!!)
                )
                else liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Server Error")
                )
            } else {
                liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Error Occurred")
                )
            }
        }
    }

    fun getMovieSynopsis(movieId: Int) {
        viewModelScope.launch {
            val response = repositoryInstance.getMovieSynopsis(movieId)
            liveDataNowPlayingMovieList.postValue(
                APIResponseStateClass.LoadingResponseClass()
            )
            if (response.isSuccessful) {
                if (response.body() != null) liveDataMovieSynopsisList.postValue(
                    APIResponseStateClass.SuccessResponseClass(response.body()!!)
                )
                else liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Server Error")
                )
            } else {
                liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Error Occurred")
                )
            }
        }
    }

    fun getMovieCredits(movieId: Int) {
        viewModelScope.launch {
            val response = repositoryInstance.getMovieCredits(movieId)
            liveDataNowPlayingMovieList.postValue(
                APIResponseStateClass.LoadingResponseClass()
            )
            if (response.isSuccessful) {
                if (response.body() != null) liveDataMoviesCredits.postValue(
                    APIResponseStateClass.SuccessResponseClass(getDisplayMovieCredit(response.body()!!))
                )
                else liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Server Error")
                )
            } else {
                liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Error Occurred")
                )
            }
        }
    }

    private fun getDisplayMovieCredit(givenResponse: MoviesCreditResponse): DisplayMovieCredits {

        return DisplayMovieCredits(
            buildList {
                givenResponse.cast.mapTo(this) {
                    ItemImageWithLabelDisplay(
                        it.profile_path,
                        it.name
                    )
                }
            },
            buildList {
                givenResponse.crew.mapTo(this) {
                    ItemImageWithLabelDisplay(
                        it.profile_path,
                        it.name
                    )
                }
            }
        )
    }

    fun getSimilarMovie(movieId: Int) {
        viewModelScope.launch {
            val response = repositoryInstance.getSimilarMovies(movieId)
            liveDataNowPlayingMovieList.postValue(
                APIResponseStateClass.LoadingResponseClass()
            )
            if (response.isSuccessful) {
                if (response.body() != null) liveDataSimilarMoviesList.postValue(
                    APIResponseStateClass.SuccessResponseClass(getDisplaySimilarMovies(response.body()!!))
                )
                else liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Server Error")
                )
            } else {
                liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Error Occurred")
                )
            }
        }
    }

    private fun getDisplaySimilarMovies(givenResponse: MoviesSimilarResponse): DisplaySimilarMovie {
        return DisplaySimilarMovie(
            buildList {
                givenResponse.results.mapTo(this) { it.poster_path }
            }
        )
    }

    fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            val response = repositoryInstance.getMovieReviews(movieId)
            liveDataNowPlayingMovieList.postValue(
                APIResponseStateClass.LoadingResponseClass()
            )
            if (response.isSuccessful) {
                if (response.body() != null) liveDataMovieReviewsList.postValue(
                    APIResponseStateClass.SuccessResponseClass(getDisplayMovieReview(response.body()!!))
                )
                else liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Server Error")
                )
            } else {
                liveDataNowPlayingMovieList.postValue(
                    APIResponseStateClass.ErrorResponseClass("Error Occurred")
                )
            }
        }
    }

    private fun getDisplayMovieReview(givenResponse: MoviesReviewResponse): DisplayMovieReview {
        return DisplayMovieReview(
            buildList {
                givenResponse.results.mapTo(this) { ItemMovieReviewDisplay(it.author, it.content.trim()) }
            }
        )
    }

    fun updateBookmarkedIdsList(givenList: List<Int>) {
        listBookmarkedMoviesIds = givenList
    }

    fun bookmarkMovie(givenMovie: ItemEachBookmarkMovie) {
        viewModelScope.launch {
            repositoryInstance.bookmarkMovie(givenMovie)
        }
    }

    fun removeBookmarkedMovie(givenMovie: ItemEachBookmarkMovie) {
        viewModelScope.launch {
            repositoryInstance.removeBookmarkedMovie(givenMovie)
        }
    }

    fun getAllBookmarkedMovieIds() = repositoryInstance.getAllBookmarkedMovieIds()

    fun getAllBookmarkedMovies() = repositoryInstance.getAllBookmarkedMovies()
}