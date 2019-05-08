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

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.eric.quickheadline.Callback;
import com.example.eric.quickheadline.bookmark.BookmarkRepository;
import com.example.eric.quickheadline.bookmark.BookmarkRepositoryImpl;
import com.example.eric.quickheadline.bookmark.BookmarkViewModel;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.PreferenceUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by eric on 05/02/2018.
 * Dagger module class
 */

@Module
public class ModuleProvider {

    // Constructor
    public ModuleProvider() {
    }

    @Provides
    @Singleton

    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    PreferenceUtils providePreferenceUtils(SharedPreferences preferences) {
        return new PreferenceUtils(preferences);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    @Named("weather")
    ApiEndpoint provideWeatherService(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantFields.WEATHER_BASE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiEndpoint.class);
    }

    @Provides
    @Singleton
    @Named("article")
    ApiEndpoint provideArticleService(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantFields.NEWS_BASE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiEndpoint.class);
    }

    @Provides
    @Singleton
    @Named("maps")
    ApiEndpoint provideMapService(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantFields.MAP_BASE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiEndpoint.class);
    }

    @Provides
    @Singleton
    @Named("geonames")
    ApiEndpoint provideGeoNameService(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ConstantFields.GEO_NAME_BASE_URL)
                .client(okHttpClient)
                .build()
                .create(ApiEndpoint.class);
    }

    @Provides
    @Singleton
    BookmarkViewModel provideBookmarkViewModel(Application application) {
        return new BookmarkViewModel(application);
    }

    @Provides
    @Singleton
    Callback provideCallback(BookmarkViewModel viewModel) {
        return viewModel;
    }

    @Provides
    @Singleton
    BookmarkRepository provideBookmarkRepository(Callback callback) {
        return new BookmarkRepositoryImpl(callback);
    }
}
