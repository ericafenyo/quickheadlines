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

package com.example.eric.quickheadline.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.eric.quickheadline.BuildConfig;
import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.DateUtils;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.eric.quickheadline.utils.ConstantFields.NEWS_BASE_URL;
import static com.example.eric.quickheadline.utils.ConstantFields.QUERY_PARAMS_PAGE_SIZE;


/**
 * Created by eric on 09/03/2018.
 */

public class ArticleRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String COUNTRY_KEY = "country";
    private static final String API_KEY = "apiKey";
    private static final String PAGE_SIZE_KEY = "pageSize";
    private static final String QUERY_PATH = "top-headlines";


    private Context mContext;
    private List<News.Article> mData = new ArrayList<>();

    public ArticleRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
    }

    public void setData(List<News.Article> mData) {
        this.mData = mData;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String country = null;
        try {
            String s = JsonUtils.readFile(mContext, ConstantFields.FILE_IO_NAME);
            JSONArray array = new JSONArray(s);
            final int countryCodeIndex = 2;
            country = String.valueOf(array.get(countryCodeIndex));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        URL url = buildArticleUrl(country);
        try {
            String httpResponse = HelperUtils.getHttpResponse(url);
            News news = JsonUtils.deSerialize(httpResponse, News.class);
            mData = news.getArticles();

        } catch (
                IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_list_widget);
        view.setTextViewText(R.id.tv_widget_title, mData.get(position).getTitle());
        String utcDate = DateUtils.getUTCDate(mData.get(position).getPublishedAt());
        view.setTextViewText(R.id.tv_widget_date, DateUtils.getLocalDate(utcDate));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public static URL buildArticleUrl(String country) {
        Uri uri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendPath(QUERY_PATH)
                .appendQueryParameter(COUNTRY_KEY, country)
                .appendQueryParameter(PAGE_SIZE_KEY, String.valueOf(QUERY_PARAMS_PAGE_SIZE))
                .appendQueryParameter(API_KEY, BuildConfig.NEWS_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
