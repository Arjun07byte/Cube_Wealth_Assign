package com.arjun.cubewealth.dataModels

data class MoviesNowPlayingResponse(
    val dates: ItemMovieDates,
    val page: Int,
    val results: List<ItemEachMovie>,
    val total_pages: Int,
    val total_results: Int
)