package app.id.devfest.articlesummarization.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Wiki::class], version = 1, exportSchema = false
)
abstract class WikiDatabase : RoomDatabase() {
    abstract fun wikiDao(): WikiDao
}