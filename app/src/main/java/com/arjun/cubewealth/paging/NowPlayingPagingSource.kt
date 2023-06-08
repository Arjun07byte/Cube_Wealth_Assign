package com.arjun.cubewealth.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arjun.cubewealth.api.ApiMovieDB
import com.arjun.cubewealth.dataModels.ItemEachMovie

class NowPlayingPagingSource(private val movieDBAPI: ApiMovieDB) :
    PagingSource<Int, ItemEachMovie>() {
    override fun getRefreshKey(state: PagingState<Int, ItemEachMovie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemEachMovie> {
        try {
            val currPosition = params.key ?: 1
            val response = movieDBAPI.getNowPlayingMovies(page = currPosition)
            return LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (currPosition == 1) null else currPosition - 1, // Only paging forward.
                nextKey = if (currPosition == response.body()!!.total_results) null else currPosition + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(Throwable(e.localizedMessage))
        }
    }
}