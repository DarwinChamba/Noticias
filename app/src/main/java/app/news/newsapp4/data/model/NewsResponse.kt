package app.news.newsapp4.data.model

import app.news.newsapp4.data.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)