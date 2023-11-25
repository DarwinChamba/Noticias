package app.news.newsapp4.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.news.newsapp4.data.util.converter.Converter
import app.news.newsapp4.data.model.Article
import app.news.newsapp4.data.util.resource.Resource

@Database(
    entities = [Article::class], version = 1, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun getDao():ArticleDao
}