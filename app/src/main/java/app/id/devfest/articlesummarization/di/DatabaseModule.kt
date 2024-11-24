package app.id.devfest.articlesummarization.di

import android.content.Context
import androidx.room.Room
import app.id.devfest.articlesummarization.core.data.local.WikiDao
import app.id.devfest.articlesummarization.core.data.local.WikiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Provide the Room Database instance
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WikiDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WikiDatabase::class.java,
            "wiki_database"
        ).build()
    }

    // Provide the FeaturedArticleDao instance
    @Provides
    @Singleton
    fun provideWikiDao(database: WikiDatabase): WikiDao {
        return database.wikiDao()
    }

}