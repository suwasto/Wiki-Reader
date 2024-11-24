package app.id.devfest.articlesummarization.di

import app.id.devfest.articlesummarization.core.data.local.WikiDao
import app.id.devfest.articlesummarization.core.data.remote.api.WikipediaApi
import app.id.devfest.articlesummarization.core.data.remote.model.FeaturedResponse
import app.id.devfest.articlesummarization.core.data.remote.model.SearchResponse
import app.id.devfest.articlesummarization.core.data.remote.model.WikiResponse
import app.id.devfest.articlesummarization.core.data.remote.repository.WikiRepository
import app.id.devfest.articlesummarization.core.data.remote.repository.WikiRepositoryImpl
import app.id.devfest.articlesummarization.core.utils.FeaturedArticlesDeserializer
import app.id.devfest.articlesummarization.core.utils.SearchResponseDeserializer
import app.id.devfest.articlesummarization.core.utils.WikiResponseDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(WikiResponse::class.java, WikiResponseDeserializer()) // WikiResponse deserializer
            .registerTypeAdapter(SearchResponse::class.java, SearchResponseDeserializer()) // SearchResponse deserializer
            .registerTypeAdapter(FeaturedResponse::class.java, FeaturedArticlesDeserializer()) // FeaturedResponse deserializer
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/") // Base URL for your API
            .addConverterFactory(GsonConverterFactory.create(gson)) // Gson converter for JSON parsing
            .build()
    }

    @Provides
    @Singleton
    fun provideWikipediaApi(retrofit: Retrofit): WikipediaApi {
        return retrofit.create(WikipediaApi::class.java) // API service interface
    }

    @Provides
    @Singleton
    fun provideWikiRepository(
        wikipediaApi: WikipediaApi,
        wikiDao: WikiDao
    ): WikiRepository {
        return WikiRepositoryImpl(wikipediaApi = wikipediaApi, wikiDao = wikiDao)
    }

}