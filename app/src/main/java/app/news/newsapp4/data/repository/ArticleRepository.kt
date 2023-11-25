package app.news.newsapp4.data.repository

import app.news.newsapp4.data.database.ArticleDatabase
import app.news.newsapp4.data.model.Article
import app.news.newsapp4.data.network.ApiSearchArticles
import app.news.newsapp4.data.network.ApiServicesGetArticles
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val listArticles: ApiServicesGetArticles,
    private val searchArticles:ApiSearchArticles,
    private val data:ArticleDatabase
) {

    suspend fun getAllArticles(countryCode:String,pageNumber:Int)=
        listArticles.getAllArticles(countryCode, pageNumber)

    suspend fun searhArticles(query:String,pageNumber:Int)=
        searchArticles.searchArticles(query, pageNumber)

    suspend fun insertArticles(article: Article)=data.getDao().insertArticle(article)
    suspend fun deleteArticle(article: Article)=data.getDao().deleteArticle(article)
    fun getAllArticlesDatabase()=data.getDao().getAllArticlesDatabase()
}