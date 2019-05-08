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

package com.example.eric.quickheadline.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.eric.quickheadline.BuildConfig;
import com.example.eric.quickheadline.Callback;
import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.contract.ArticleEntry;
import com.example.eric.quickheadline.di.ApiEndpoint;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.JsonUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by eric on 14/02/2018.
 * a simple class that syncs the News data between its endpoint and our local database
 * The endpoint uses specific params from a file stored locally.
 * and these params are based on the users location settings;
 */

public class ArticleSyncTask {

    private static final String TAG = ArticleSyncTask.class.getName();

    synchronized static public void execute(Context mContext, Callback mCallback) {

        Log.v(TAG,"ArticleSyncTask");

        /* 1. get an object of ApiEndpoint using dagger*/
        ApiEndpoint endpoint = ((MyApp) mContext).getComponent().getArticleEndpoint();
        SharedPreferences preferences = ((MyApp) mContext).getComponent().getSharedPreferences();
        PreferenceUtils preferenceUtils = new PreferenceUtils(preferences);

        /*2 .reads data from file
            This data is parsed and used as query params
            The data schema

            * [
            * 0 : "lat",
            * 1 : "lng",
            * 2 : "countryCode",
            * 3 : "languages",
            * 4 : "countryName"
            * ]
            *
            * The country code with index 2 is used to request country-specific News Articles
        * */
        String country = null;
        try {
            String s = JsonUtils.readFile(mContext, ConstantFields.FILE_IO_NAME);
            JSONArray array = new JSONArray(s);
            final int countryCodeIndex = 2;
            country = String.valueOf(array.get(countryCodeIndex));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        /*3. make calls to the endpoint and store data in local database*/
        endpoint.getArticle(country, ConstantFields.QUERY_PARAMS_PAGE_SIZE, BuildConfig.NEWS_API_KEY).enqueue(new retrofit2.Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                List<News.Article> articles;

                if (response.body() != null) {
                    articles = response.body().getArticles();
                    if (articles.size() == 0) {
                        mCallback.onEmpty();
                        preferenceUtils.setCallback(mContext.getString(R.string.callback_msg_on_empty));
                    } else {
                        mCallback.onSuccess(articles);
                        preferenceUtils.setCallback(mContext.getString(R.string.callback_msg_on_success));
                        //updates our ContentProvider
                        Log.e(TAG,articles.toString());
                        syncArticle(mContext, articles);
                    }

                } else {
                    Log.e(TAG, "response is null");
                    mCallback.onNull();
                    preferenceUtils.setCallback(mContext.getString(R.string.callback_msg_on_null));
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_on_network_call) + t);
                mCallback.onFailure(t);
                preferenceUtils.setCallback(mContext.getString(R.string.callback_msg_on_failure));
            }
        });
    }

    /**
     * Helper method for saving the data into out database
     *
     * @param context The context to use. {@link android.app.Application}
     */
    private static void syncArticle(Context context, List<News.Article> articles) {

        ContentValues[] articleContentValues = JsonUtils.getArticleContentValues(articles);
        if (articleContentValues != null && articleContentValues.length != 0) {
                    /* Get a handle on the ContentResolver to delete and insert data */
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.delete(ArticleEntry.CONTENT_URI_ARTICLE, null,
                    null);
                    /* Insert our new Article data into the ContentProvider */
            contentResolver.bulkInsert(ArticleEntry.CONTENT_URI_ARTICLE,
                    articleContentValues);
        }
    }

}