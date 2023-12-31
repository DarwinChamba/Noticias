package app.news.newsapp4.data.util.resource

sealed class Resource<T>(
    val data:T?=null,
    val message:String?=null
) {
    class Error<T>(message: String?,data: T?=null): Resource<T>(data, message)
    class Success<T>(data: T?=null): Resource<T>(data)
    class Loading<T>(): Resource<T>()
}