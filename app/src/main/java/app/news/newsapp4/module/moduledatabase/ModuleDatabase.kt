package app.news.newsapp4.module.moduledatabase

import android.content.Context
import androidx.room.Room
import app.news.newsapp4.data.database.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
class ModuleDatabase {

    @Singleton
    @Provides
    fun getProvidesDatabase(@ApplicationContext context: Context)=
        Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "articleData"
        ).build()

    @Singleton
    @Provides
    fun getDao(data:ArticleDatabase)=data.getDao()
}