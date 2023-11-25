package app.news.newsapp4.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiServicesGetArticles @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getAllArticles(countryCode:String,pageNumber:Int)=
        withContext(Dispatchers.IO){
            apiClient.getAllArticles(countryCode, pageNumber)
        }
}