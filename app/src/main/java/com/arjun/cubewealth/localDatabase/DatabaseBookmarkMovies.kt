package com.arjun.cubewealth.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arjun.cubewealth.dataModels.ItemEachBookmarkMovie

// Class to Initiate and get the ROOM Database
// instance scoped with the given application context
@Database(
    entities = [ItemEachBookmarkMovie::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseBookmarkMovies : RoomDatabase() {
    abstract fun getBookmarkMoviesDAO(): DAOBookmarkMovies

    companion object {
        @Volatile
        private var databaseInstance: DatabaseBookmarkMovies? = null
        private val LOCK = Any()

        operator fun invoke(currContext: Context) = databaseInstance ?: synchronized((LOCK)) {
            databaseInstance ?: createDatabase(currContext)
        }

        private fun createDatabase(currContext: Context) =
            Room.databaseBuilder(
                currContext.applicationContext,
                DatabaseBookmarkMovies::class.java,
                "db_bookmarks_movies.db"
            ).build()
    }
}