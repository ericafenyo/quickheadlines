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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.home.WeatherDetailActivity;

import java.util.Locale;

/**
 * a simple class to build notification
 */
public class NotificationUtils {
    private static final String LOG_TAG = "NotificationUtils";

    //Columns that should be included in the query
    private static final String[] WEATHER_PROJECTION = {
            WeatherEntry.COLUMN_NAME_TEMPERATURE,
            WeatherEntry.COLUMN_NAME_SUMMARY,
            WeatherEntry.COLUMN_NAME_ICON
    };

    public static void showNotification(Context context) {
        SharedPreferences preferences = ((MyApp) context).getComponent().getSharedPreferences();
        PreferenceUtils preferenceUtils = new PreferenceUtils(preferences);
        Cursor cursor;

        try {
            //query data from database
            cursor = context.getContentResolver().query(
                    WeatherEntry.CONTENT_URI_WEATHER,
                    WEATHER_PROJECTION,
                    null,
                    null,
                    null);

            //proceed to show notification only if cursor is not null and empty
            if (cursor != null && cursor.moveToFirst()) {

                //column Indices
                int index_summary = cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_SUMMARY);
                int index_temperature = cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_TEMPERATURE);
                int index_icon = cursor.getColumnIndex(WeatherEntry.COLUMN_NAME_ICON);

                //*gets data at the respective column indices
                double temperature = cursor.getDouble(index_temperature);
                String summary = cursor.getString(index_summary);
                String icon = cursor.getString(index_icon);

                //Drawable resources ID for the notification icon
                int resourcesId = ForecastUtils.getDrawableForWeatherCondition(context, icon);

                //The notification message (String)
                String notificationText = buildNotificationContentText(context, temperature);

                //The notification ID
                final int notification_id = 703609;

                //The notification chanel ID
                final String notificationChannelId = "com.example.eric.quickheadline.NOTIFICATION_CHANNEL_ID";

                //he notification chanel name
                final CharSequence notificationChannelName = "quickheadline Channel";


                NotificationManager notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                //Create notification channel
                int importance;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    importance = NotificationManager.IMPORTANCE_LOW;
                    NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, notificationChannelName, importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }

                //Build the notification
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, notificationChannelId)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(resourcesId)
                        .setContentTitle(summary)
                        .setContentText(notificationText)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                //Intent to launch the app
                Intent detailIntentForToday = new Intent(context, WeatherDetailActivity.class);
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
                PendingIntent resultPendingIntent = taskStackBuilder
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                notificationBuilder.setContentIntent(resultPendingIntent);

                //set notification id
                if (notificationManager != null) {
                    notificationManager.notify(notification_id, notificationBuilder.build());
                }

                /*save the current time*/
                preferenceUtils.saveLastNotificationTime(System.currentTimeMillis());
            }

        /* close cursor */
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, context.getString(R.string.error_weather_notification) + e);
            e.printStackTrace();
        }
    }

    private static String buildNotificationContentText(Context context, double tempFahrenheit) {
        //convert degrees Fahrenheit to degrees Celsius
        double tempCelsius = ForecastUtils.convertFahrenheitToCelsius(tempFahrenheit);
        return String.format(Locale.getDefault(), context.getString(R.string.format_temperature_celsius_notification), tempCelsius);
    }
}
