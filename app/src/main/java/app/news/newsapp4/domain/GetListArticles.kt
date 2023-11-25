package app.news.newsapp4.domain

import app.news.newsapp4.data.repository.ArticleRepository
import javax.inject.Inject

class GetListArticles @Inject constructor(
    private val repository: ArticleRepository
){
   suspend operator fun invoke(countryCode:String,pageNumber:Int)=
      repository.getAllArticles(countryCode, pageNumber)
}