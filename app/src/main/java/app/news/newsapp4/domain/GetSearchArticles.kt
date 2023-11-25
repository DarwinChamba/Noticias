package app.news.newsapp4.domain

import app.news.newsapp4.data.repository.ArticleRepository
import javax.inject.Inject

class GetSearchArticles @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend  operator fun invoke(query:String,pageNumber:Int)=
        repository.searhArticles(query, pageNumber)
}