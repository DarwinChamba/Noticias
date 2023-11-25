package app.news.newsapp4.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.news.newsapp4.data.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article:Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("select * from tablaDatos")
    fun getAllArticlesDatabase():LiveData<List<Article>>
}