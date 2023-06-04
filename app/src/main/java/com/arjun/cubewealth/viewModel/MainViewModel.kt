package com.arjun.cubewealth.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.dataModels.ItemEachMovie
import com.arjun.cubewealth.dataModels.MoviesNowPlayingResponse
import com.arjun.cubewealth.repository.MainRepository
import com.arjun.cubewealth.utills.APIResponseStateClass
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryInstance: MainRepository
) : ViewModel() {
    val liveDataNowPlayingMovieList: MutableLiveData<APIResponseStateClass<MoviesNowPlayingResponse>> =
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