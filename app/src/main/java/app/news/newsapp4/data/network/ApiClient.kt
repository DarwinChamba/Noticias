package app.news.newsapp4.data.network

import app.news.newsapp4.data.util.constants.Constants.Companion.KEY
import app.news.newsapp4.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("v2/top-headlines")
    suspend fun getAllArticles(
        @Query("country")countryCode:String="us",
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchArticles(
        @Query("q")search:String,
        @Query("page") pageNumber:Int=1,
        @Query("apiKey") apiKey:String=KEY
    ): Response<NewsResponse>
}