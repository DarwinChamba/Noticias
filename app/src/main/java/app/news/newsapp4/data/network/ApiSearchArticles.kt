package app.news.newsapp4.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiSearchArticles @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun searchArticles(query:String,pageNumber: Int)=
        withContext(Dispatchers.IO){
            apiClient.searchArticles(query,pageNumber)
        }
}