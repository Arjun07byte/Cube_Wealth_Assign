package com.arjun.cubewealth.dataModels

data class DisplayMovieCredits(
    val movieCastList: List<ItemImageWithLabelDisplay>,
    val movieCrewList: List<ItemImageWithLabelDisplay>
)
