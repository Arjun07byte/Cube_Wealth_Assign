package com.arjun.cubewealth.localDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie

// Data Annotation Object or DAO which will be used
// to access the functionalities needful for operating
// data in the ROOM Database like Insertion, Deletion
// and fetching the Data
@Dao
interface DAOBookmarkMovies {
    // function to insert a newBookmarkMovie Item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkMovie(givenMovieItem: ItemEachBookmarkMovie): Long

    // function to remove a givenMovieItem from the
    // BookmarkedMovies tables
    @Delete
    suspend fun removeBookmarkedMovie(givenMovieItem: ItemEachBookmarkMovie)

    // Function to Query and access all the stored
    // bookmarked movies
    @Query("SELECT * from MoviesTable")
    fun getAllBookmarkedMovies(): LiveData<List<ItemEachBookmarkMovie>>
}