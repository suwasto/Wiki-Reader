package app.id.devfest.articlesummarization.di

import app.id.devfest.articlesummarization.screens.component.WikiListMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MapperModule {

    @Provides
    fun provideWikiListMapper(): WikiListMapper {
        return WikiListMapper()
    }

}