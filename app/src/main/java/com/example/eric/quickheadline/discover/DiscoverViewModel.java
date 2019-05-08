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

package com.example.eric.quickheadline.discover;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.quickheadline.BuildConfig;
import com.example.eric.quickheadline.di.ApiEndpoint;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eric on 13/03/2018.
 * a viewModel class that
 * prepares and stores observable data for an observer
 */

public class DiscoverViewModel extends AndroidViewModel {
    private static final String TAG = DiscoverViewModel.class.getName();
    private MutableLiveData<List<News.Article>> mArticleByCategory;
    private String mCategory;


    @Inject
    @Named("article")
    ApiEndpoint endpoint;

    public DiscoverViewModel(@NonNull Application application, String mCategory) {
        super(application);
        this.mCategory = mCategory;
        ((MyApp) getApplication()).getComponent().inject(this);
    }

    /**
     * @return category specific Articles
     */
    public LiveData<List<News.Article>> getArticleByCategory() {
        if (mArticleByCategory == null) {
            mArticleByCategory = new MutableLiveData<>();
            loadArticleByCategory();
        }
        return mArticleByCategory;
    }


    public void loadArticleByCategory() {
        String country = null;
        try {
            String s = JsonUtils.readFile(getApplication(), ConstantFields.FILE_IO_NAME);
            JSONArray array = new JSONArray(s);
            final int countryCodeIndex = 2;
            country = String.valueOf(array.get(countryCodeIndex));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        endpoint.getCategory(mCategory, country, ConstantFields.QUERY_PARAMS_PAGE_SIZE, BuildConfig.NEWS_API_KEY).enqueue(new Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.code() == 200) {
                    mArticleByCategory.setValue(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.v(TAG, "Response error: " + t);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ((MyApp) getApplication()).getComponent().getPreferenceUtils().setFirstTimeChecked(false);
        Log.v(TAG, "onCleared()");
    }

}
