package app.news.newsapp4.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import android.widget.AbsListView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.news.newsapp4.data.model.Article
import app.news.newsapp4.data.model.NewsResponse
import app.news.newsapp4.data.repository.ArticleRepository
import app.news.newsapp4.data.util.resource.Resource
import app.news.newsapp4.domain.GetListArticles
import app.news.newsapp4.domain.GetSearchArticles
import app.news.newsapp4.ui.application.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    app: Application,
    private val listArticle: GetListArticles,
    private val searchArticles: GetSearchArticles,
    private val repository: ArticleRepository


) : AndroidViewModel(app) {
    val pageNumber = 1
    val listaNewsResponse = MutableLiveData<Resource<NewsResponse>>()

    val pageNumberSearch = 1
    val searchNewsResponse = MutableLiveData<Resource<NewsResponse>>()

    var newsResponse: NewsResponse? = null
    var newsResponseSearch: NewsResponse? = null


    fun getAllArticles(countryCode: String) = viewModelScope.launch {
      safeBreakingsNews(countryCode)
    }

    private suspend fun safeBreakingsNews(countryCode: String) {
        listaNewsResponse.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = listArticle(countryCode, pageNumber)
                listaNewsResponse.postValue(handleBreakingsNews(response))
            }else{
                listaNewsResponse.postValue(Resource.Error("no hay internet"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> listaNewsResponse.postValue(Resource.Error("no hay internet"))
                else -> listaNewsResponse.postValue(Resource.Error("error de conversion"))
            }
        }
    }

    private fun handleBreakingsNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {

                if (newsResponse == null) {
                    newsResponse = it
                } else {
                    val oldArticles = newsResponse?.articles
                    val newsArticles = it.articles
                    oldArticles?.addAll(newsArticles)
                }
                return Resource.Success(newsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchArticles(query: String) = viewModelScope.launch {
        safeSearchArticles(query)

    }
    private suspend fun safeSearchArticles(query: String){
        searchNewsResponse.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = searchArticles(query, pageNumberSearch)
                searchNewsResponse.postValue(handleSearchArticles(response))
            }else{
                searchNewsResponse.postValue(Resource.Error("no hay internet"))
            }

        }catch (t:Throwable){
            when(t){
                is Throwable->searchNewsResponse.postValue(Resource.Error("error de conversion"))
                else -> searchNewsResponse.postValue(Resource.Error("no hay internet"))
            }
        }
    }

    private fun handleSearchArticles(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (newsResponseSearch == null) {
                    newsResponseSearch = it
                } else {
                    val oldArticles = newsResponseSearch?.articles
                    val newList = it.articles
                    oldArticles?.addAll(newList)
                }
                return Resource.Success(newsResponseSearch ?: it)
            }
        }
        return Resource.Error(response.message())
    }
    //metodos de la base de datos

    fun getAllArticlesDatabase() = repository.getAllArticlesDatabase()
    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        repository.insertArticles(article)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> return false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_ETHERNET -> true
                    TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return false

    }


}