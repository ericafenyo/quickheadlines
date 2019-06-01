package com.ericafenyo.data.news

import okhttp3.Interceptor
import okhttp3.Response

class NewsPathInterceptor(private val apiKey: String) : Interceptor {

    companion object {
        private const val API_TOKEN_KEY = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().newBuilder().addQueryParameter(API_TOKEN_KEY, apiKey).build()
        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}