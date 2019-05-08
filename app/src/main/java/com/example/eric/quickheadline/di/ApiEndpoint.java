/*
 * Copyright (C) 2018 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.quickheadline.di;


import com.example.eric.quickheadline.model.Country;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eric on 03/02/2018.
 * the difference API endpoints are defined here
 */

public interface ApiEndpoint {

    ////////// Weather ApiEndpoint ////////////////////
    @GET("/forecast/{apiKey}/{lat},{lng}")
    Call<Weather> getWeather(@Path("apiKey") String apiKey,
                             @Path("lat") String lat,
                             @Path("lng") String lng,
                             @Query("exclude") String exclude,
                             @Query("lang") String lang);

    ////////// GeoNames ApiEndpoint ////////////////////
    @GET("countryCodeJSON")
    Call<Country> getCountry(@Query("lat") String lat,
                             @Query("lng") String lng,
                             @Query("username") String username);

    ////////// News ApiEndpoint ////////////////////
    @GET("top-headlines")
    Call<News> getArticle(@Query("country") String country,
                          @Query("pageSize") int pageSize,
                          @Query("apiKey") String apiKey);

    ////////// News Category ApiEndpoint ////////////////////
    @GET("top-headlines")
    Call<News> getCategory(@Query("category") String category,
                           @Query("country") String country,
                           @Query("pageSize") int pageSize,
                           @Query("apiKey") String apiKey);


    ////////// News Search ApiEndpoint ////////////////////
    @GET("everything")
    Call<News> getSearchRepose(@Query("q") String param,
                               @Query("pageSize") int pageSize,
                               @Query("apiKey") String apiKey);
}