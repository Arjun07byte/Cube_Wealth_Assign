package com.arjun.cubewealth.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie
import com.arjun.cubewealth.dataModels.ItemEachMovie
import com.arjun.cubewealth.repository.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val repositoryInstance: MainRepository
) : ViewModel() {

    // Utility function which will be used to
    // convert the online fetched movieItem to bookmarkMovie
    // Item which takes less storage and thus better UX
    private fun makeBookmarkItem(givenMovie: ItemEachMovie): ItemEachBookmarkMovie =
        ItemEachBookmarkMovie(
            givenMovie.movieId, givenMovie.backdrop_path, givenMovie.title, givenMovie.release_date
        )
}