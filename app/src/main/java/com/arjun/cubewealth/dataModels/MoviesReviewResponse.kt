package com.arjun.cubewealth.dataModels

data class MoviesReviewResponse(
    val id: Int,
    val page: Int,
    val results: List<ItemEachReview>,
    val total_pages: Int,
    val total_results: Int
)