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

package com.example.eric.quickheadline.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eric on 22/02/2018.
 * class that defines the SQLite database table contents
 */
public class WeatherEntry implements BaseColumns {

    /* The Content authority serves as a name for the provider (Content Provider) */
    public static final String WEATHER_CONTENT_AUTHORITY = "com.example.eric.quickheadline.provider.weatherprovider";

    /*
     * The CONTENT_AUTHORITY is used to create the base of all URI's which apps will use to contact
     * the content provider
     */
    public static final Uri CURRENT_WEATHER_BASE_CONTENT_URI = Uri.parse("content://" + WEATHER_CONTENT_AUTHORITY);

    public static final String PATH_CURRENT_WEATHER = "currently";

    /* The CONTENT_URI used to query the weather database table */
    public static final Uri CONTENT_URI_WEATHER = CURRENT_WEATHER_BASE_CONTENT_URI
            .buildUpon()
            .appendPath(PATH_CURRENT_WEATHER)
            .build();

    /* Defines the name of the table */
    public static final String TABLE_NAME = "currently";

    /* Column names for the  weather. All these details are stored
    * as returned by the API
    */

    /* TIMEZONE is stored as a String representing the IANA timezone name for the
    requested location.*/
    public static final String COLUMN_NAME_TIMEZONE = "timezone";

    /*SUMMARY is stored as a String representing a human-readable text summary of the weather
     condition*/
    public static final String COLUMN_NAME_SUMMARY = "summary";

    /*TEMPERATURE is stored as a double representing the air temperature in degrees Fahrenheit */
    public static final String COLUMN_NAME_TEMPERATURE = "temperature";

    /* APPARENT_TEMPERATURE is stored as a double representing the apparent (or “feels like”) temperature in degrees Fahrenheit */
    public static final String COLUMN_NAME_APPARENT_TEMPERATURE = "apparent_temperature";

    /* HUMIDITY is stored as a double representing the relative humidity, between 0 and 1*/
    public static final String COLUMN_NAME_HUMIDITY = "humidity";

    /* WIND_SPEED is stored as a double in miles per hour */
    public static final String COLUMN_NAME_WIND_SPEED = "wind_speed";


    /* VISIBILITY is stored as a float representing the average visibility in miles, capped
    at 10 miles. */
    public static final String COLUMN_NAME_VISIBILITY = "visibility";

    /*TIME is stored as a long representing the UNIX time at which this data point begins. */
    public static final String COLUMN_NAME_TIME = "time";

    /* ICON is stored as a String representing a machine-readable text summary of the
    weather condition, suitable for selecting an icon for display. This property
    will have cloudy, partly-cloudy-day, partly-cloudy-night, etc. */
    public static final String COLUMN_NAME_ICON = "icon";

    public static final String COLUMN_NAME_PRESSURE = "pressure";

}
