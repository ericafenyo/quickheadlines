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

package com.example.eric.quickheadline.utils;

import android.content.ContentValues;
import android.content.Context;

import com.example.eric.quickheadline.contract.ArticleEntry;
import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.model.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by eric on 08/02/2018.
 */

public class JsonUtils {

    /**
     * puts the weather information into a contentValues to be inserted into a database table
     *
     * @param weather an object of {@link Weather}
     * @return a ContentValue of weather information
     */
    public static ContentValues getWeatherContentValues(Weather weather) {
        Weather.Currently currently = weather.getCurrently();

        ContentValues values = new ContentValues();
        values.put(WeatherEntry.COLUMN_NAME_TIMEZONE, weather.getTimezone());
        values.put(WeatherEntry.COLUMN_NAME_TIME, currently.getTime());
        values.put(WeatherEntry.COLUMN_NAME_APPARENT_TEMPERATURE, currently.getApparentTemperature());
        values.put(WeatherEntry.COLUMN_NAME_HUMIDITY, currently.getHumidity());
        values.put(WeatherEntry.COLUMN_NAME_ICON, currently.getIcon());
        values.put(WeatherEntry.COLUMN_NAME_SUMMARY, currently.getSummary());
        values.put(WeatherEntry.COLUMN_NAME_TEMPERATURE, currently.getTemperature());
        values.put(WeatherEntry.COLUMN_NAME_VISIBILITY, currently.getVisibility());
        values.put(WeatherEntry.COLUMN_NAME_WIND_SPEED, currently.getWindSpeed());
        values.put(WeatherEntry.COLUMN_NAME_PRESSURE, currently.getPressure());

        return values;
    }


    /**
     * puts the list of Article into contentValues to be inserted into a database table
     *
     * @param articles list of {@link News.Article}
     * @return a ContentValues of Articles
     */
    public static ContentValues[] getArticleContentValues(List<News.Article> articles) {

        int index = 0;
        ContentValues[] newsArticleValues = new ContentValues[articles.size()];
        for (News.Article article : articles) {
            ContentValues values = new ContentValues();
            values.put(ArticleEntry.COLUMN_NAME_SOURCE, article.getSource().getName());
            values.put(ArticleEntry.COLUMN_NAME_AUTHOR, article.getAuthor());
            values.put(ArticleEntry.COLUMN_NAME_TITLE, article.getTitle());
            values.put(ArticleEntry.COLUMN_NAME_DESCRIPTION, article.getDescription());
            values.put(ArticleEntry.COLUMN_NAME_URL_TO_IMAGE, article.getUrlToImage());
            values.put(ArticleEntry.COLUMN_NAME_URL, article.getUrl());
            values.put(ArticleEntry.COLUMN_NAME_PUBLISHED_DATE, article.getPublishedAt());

            newsArticleValues[index++] = values;
        }
        return newsArticleValues;
    }

    /**
     * @param context
     * @param stringData
     * @throws IOException
     */
    public static void writeFile(Context context, String fileName, String stringData) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        if (stringData != null && stringData.length() != 0) {
            outputStream.write(stringData.getBytes());
            outputStream.close();
        }
    }

    public static String readFile(Context context, String fileName) throws IOException {
        StringBuffer buffer = new StringBuffer();
        FileInputStream inputStream = context.openFileInput(fileName);
        int read;
        while ((read = inputStream.read()) != -1) {
            buffer.append((char) read);
        }
        return buffer.toString();
    }

    /**
     * Serialize string.
     *
     * @param <T> Type of the object passed
     * @param obj Object to serialize
     * @return Serialized string
     */
    public static <T> String serialize(T obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);

    }

    /**
     * De-serialize a string
     *
     * @param <T>        Type of the object
     * @param jsonString Serialized string
     * @param tClass     Class of the type
     * @return De-serialized object
     * @throws ClassNotFoundException the class not found exception
     */
    public static <T> T deSerialize(String jsonString, Class<T> tClass) throws ClassNotFoundException {
        if (!isValid(jsonString)) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, tClass);
    }

    /**
     * De-serialize to list
     *
     * @param <T>  Type of the object
     * @param data Serialized string
     * @param type Type of the object
     * @return List of converted object
     */
    public static <T> List<T> deSerializeList(String data, Type type) {
        if (!isValid(data)) {
            return null;
        }
        return new Gson().fromJson(data, type);
    }

    /**
     * Check if a string is valid json.
     *
     * @param json Json String
     * @return Flag indicating if string is json
     */
    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

    /**
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     * @return Json in a string format
     */
    public static String loadJSONFromAsset(Context context, String fileName) throws IOException {
        String json;
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        return json;
    }
}
