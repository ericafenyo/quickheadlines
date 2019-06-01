package com.ericafenyo.data.weather

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Weather(apiKey: String) {
    companion object {
        private const val NEWS_BASE_URL = "https://api.nytimes.com/svc/topstories/v2/"
    }

    private val gson: Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(WeatherPathInterceptor(apiKey))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NEWS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
        .client(okHttpClient)
        .build()

    val weatherService = retrofit.create(WeatherService::class.java)

}
