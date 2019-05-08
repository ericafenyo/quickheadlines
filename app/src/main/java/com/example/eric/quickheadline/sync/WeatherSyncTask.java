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
import android.text.format.DateUtils;
import android.util.Log;

import com.example.eric.quickheadline.BuildConfig;
import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.di.ApiEndpoint;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.Weather;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.JsonUtils;
import com.example.eric.quickheadline.utils.NotificationUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.eric.quickheadline.utils.ConstantFields.FILE_IO_NAME;


/**
 * Created by eric on 08/02/2018.
 * a simple class that sync the weather info between its endpoint and our local database
 * The endpoint uses specific params from a file stored locally.
 */

public class WeatherSyncTask {
    private static final String TAG = WeatherSyncTask.class.getName();

    synchronized public static void execute(Context context) {
        Log.v(TAG, "WeatherSyncTask");

        /* 1. get an object of ApiEndpoint using dagger*/
        ApiEndpoint weatherEndpoint = ((MyApp) context).getComponent().getWeatherEndpoint();



         /* 2. reads data from file
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
            * The lat code with index 0 represents latitude geo-coordinate
            * The lng code with index 1 represents longitude geo-coordinate
            * The languages with index 3 is used for requesting language-specific weather info based on the country.
        * */
        String lat = null;
        String lng = null;
        String language = null;
        try {
            String s = JsonUtils.readFile(context, FILE_IO_NAME);
            JSONArray array = new JSONArray(s);
            final int latIndex = 0;
            final int lngIndex = 1;
            final int languageIndex = 3;
            lat = (String) array.get(latIndex);
            lng = (String) array.get(lngIndex);
            language = String.valueOf(array.get(languageIndex));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        /*3. make calls to the endpoint and store data in local database*/
        weatherEndpoint.getWeather(BuildConfig.WEATHER_API_KEY, lat, lng, ConstantFields.QUERY_PARAMS_EXCLUDE, language).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                if (response.body() != null) {
                    //updates our ContentProvider
                    syncWeather(context, response.body());
                } else {
                    Log.e(TAG, "response is null");
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, "Failed to sync Weather Info " + t);
            }
        });
    }


    /**
     * Helper method for saving the data into out database
     *
     * @param context The context to use. {@link android.app.Application}
     * @param weather {@link Weather} object
     */
    private static void syncWeather(Context context, Weather weather) {
        PreferenceUtils preferenceUtils = ((MyApp) context).getComponent().getPreferenceUtils();
        ContentValues currentWeatherValues = JsonUtils.getWeatherContentValues(weather);

        if (currentWeatherValues != null && currentWeatherValues.size() != 0) {

                    /* Get a handle on the ContentResolver to delete and insert data */
            ContentResolver contentResolver = context.getContentResolver();

            contentResolver.delete(WeatherEntry.CONTENT_URI_WEATHER, null,
                    null);

                    /* Insert our new weather data into the ContentProvider */
            contentResolver.insert(WeatherEntry.CONTENT_URI_WEATHER,
                    currentWeatherValues);


                /*notify the user that the weather has been refreshed*/
            boolean notificationsEnabled = preferenceUtils.isNotificationEnabled();

                /*
                 * If the last notification was shown was more than 1 day ago, we want to send
                 */
            long timeSinceLastNotification = preferenceUtils.getElapsedTimeSinceLastNotification();

            boolean oneDayPassedSinceLastNotification = false;

            if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                oneDayPassedSinceLastNotification = true;
            }
                /*show the notification if the user enabled it*/
            if (notificationsEnabled && oneDayPassedSinceLastNotification) {
                NotificationUtils.showNotification(context);
            }

        }
    }
}

