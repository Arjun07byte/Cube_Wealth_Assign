package com.arjun.cubewealth.dataModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "MoviesTable"
)
data class ItemEachBookmarkMovie(
    @PrimaryKey
    val movie_id: Int,
    val backdropPicPath: String,
    val movie_title: String,
    val release_date: String,
)