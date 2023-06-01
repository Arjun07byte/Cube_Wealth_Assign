package com.arjun.cubewealth.dataModels

data class MoviesCreditResponse(
    val cast: List<ItemMovieCast>,
    val crew: List<ItemMovieCrew>,
    val id: Int
)