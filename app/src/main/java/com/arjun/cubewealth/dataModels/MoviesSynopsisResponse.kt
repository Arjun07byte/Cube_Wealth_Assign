package com.arjun.cubewealth.dataModels

data class MoviesSynopsisResponse(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to: ItemBelongsTo,
    val budget: Int,
    val genres: List<ItemMovieGenre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ItemProductionCompany>,
    val production_countries: List<ItemProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<ItemSpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)