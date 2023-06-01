package com.arjun.cubewealth.dataModels

data class MoviesSimilarResponse(
    val page: Int,
    val results: List<ItemEachMovie>,
    val total_pages: Int,
    val total_results: Int
)