package app.news.newsapp4.module.moduleretrofit

import app.news.newsapp4.data.util.constants.Constants.Companion.BASE_URL
import app.news.newsapp4.data.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModuleRetrofit {
    @Singleton
    @Provides
    fun getProvidesRetrofit(): Retrofit {
        val httppLogging=HttpLoggingInterceptor()
        httppLogging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client=OkHttpClient.Builder()
            .addInterceptor(httppLogging)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun getProvidesApi(retrofit: Retrofit)=retrofit.create(ApiClient::class.java)
}