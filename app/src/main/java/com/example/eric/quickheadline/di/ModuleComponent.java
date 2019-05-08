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

import android.content.SharedPreferences;

import com.example.eric.quickheadline.home.ArticleDetailFragment;
import com.example.eric.quickheadline.discover.CategoryActivity;
import com.example.eric.quickheadline.home.MainActivity;
import com.example.eric.quickheadline.SplashActivity;
import com.example.eric.quickheadline.WelcomeActivity;
import com.example.eric.quickheadline.bookmark.BookmarkAdapter;
import com.example.eric.quickheadline.bookmark.BookmarkViewModel;
import com.example.eric.quickheadline.discover.DiscoverViewModel;
import com.example.eric.quickheadline.home.HomeFragment;
import com.example.eric.quickheadline.home.SearchActivity;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by eric on 05/02/2018.
 * Dagger component class
 */

@Component(modules = {AppModule.class, ModuleProvider.class})
@Singleton
public interface ModuleComponent {

    void inject(PreferenceUtils preferenceUtils);

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(WelcomeActivity welcomeActivity);

    void inject(BookmarkViewModel viewModel);

    void inject(DiscoverViewModel discoverViewModel);

    void inject(HomeFragment homeFragment);

    void inject(ArticleDetailFragment articleDetailFragment);

    void inject(CategoryActivity categoryActivity);

    void inject(SearchActivity searchActivity);

    void inject(BookmarkAdapter bookmarkAdapter);

    SharedPreferences getSharedPreferences();

    PreferenceUtils getPreferenceUtils();

    @Named("article")
    ApiEndpoint getArticleEndpoint();

    @Named("weather")
    ApiEndpoint getWeatherEndpoint();


}

