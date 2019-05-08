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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.di.MyApp;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by eric on 09/02/2018.
 */

public class WeatherSyncUtils {

    private static final String LOG_TAG = WeatherSyncUtils.class.getName();//for debugging purpose

    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String WEATHER_SYNC_TAG = "weather-sync";

    private static boolean sInitialized;

    public static void scheduleWeatherJobDispatcherSync(@NonNull final Context context) {
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Log.v(LOG_TAG, "scheduleWeatherJobDispatcherSync()");
        Job weatherJob = jobDispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(WeatherJobService.class)
                // tag that uniquely identifies the job
                .setTag(WEATHER_SYNC_TAG)
                //this Job should run on any network.
                .setConstraints(Constraint.ON_ANY_NETWORK)
                //persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                /* call the builder's build method to return the Job  once it is ready*/
                .build();

        /* Schedule the Job with the dispatcher */
        jobDispatcher.schedule(weatherJob);

    }

    /**
     * initialize the job and sync data between the Source and the local database
     *
     * @param context Context
     */
    synchronized public static void initialize(@NonNull final Context context) {
        Log.v(LOG_TAG, "WeatherSyncUtils");

        SharedPreferences sharedPreferences = ((MyApp) context).getComponent().getSharedPreferences();


        /* only run when it is the first time */
        if (sInitialized) return;

        sInitialized = true;

        /*starts the job */
        scheduleWeatherJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(() -> {
            Uri contentUriCurrentWeather = WeatherEntry.CONTENT_URI_WEATHER;

            /* Here, we perform the query to check to see if we have any weather data */
            Cursor cursor = context.getContentResolver().query(
                    contentUriCurrentWeather,
                    null,
                    null,
                    null,
                    null);
               /*sync immediately if the Cursor was null OR if it was empty*/
            if (null == cursor || cursor.getCount() == 0) {
                startImmediateSync(context);

            }
            if (cursor != null) {
                cursor.close();
            }
        });

        /* start the thread*/
        checkForEmpty.start();
    }

    /**
     * perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Log.v(LOG_TAG, "weather startImmediateSync()");
        Intent intent = new Intent(context, WeatherSyncIntentService.class);
        context.startService(intent);
    }
}
