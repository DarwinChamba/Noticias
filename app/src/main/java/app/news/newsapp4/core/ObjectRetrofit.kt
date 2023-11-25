package app.news.newsapp4.core

import app.news.newsapp4.data.util.constants.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ObjectRetrofit {
    fun getRetrofit(): Retrofit {
        val httpLogging=HttpLoggingInterceptor()
        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client =OkHttpClient.Builder()
            .addInterceptor(httpLogging)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}