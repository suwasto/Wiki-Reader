package app.id.devfest.articlesummarization.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WikiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Wiki>)

    @Query("SELECT * FROM wiki ORDER BY ROWID DESC LIMIT 10")
    fun getLast10Wiki(): Flow<List<Wiki>>

    @Query("SELECT * FROM wiki WHERE title LIKE '%' || :title || '%'")
    fun getWikiByTitles(title: String): Flow<List<Wiki>>

    @Query("SELECT * FROM wiki WHERE isBookmarked = 1 ORDER BY ROWID DESC")
    fun getAllBookmarkedWikis(): Flow<List<Wiki>>

    @Update
    suspend fun updateWiki(wiki: Wiki)

}