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

/**
 * Created by eric on 06/02/2018.
 * This class contains constant variables used throughout the app. They can be accessed
 * through calls to ConstantFields#ConstantName.
 */

public class ConstantFields {

    /*constants used for Intent Extra*/
    public static final String EXTRA_ARTICLE_LIST = "com.example.eric.quickheadline.EXTRA_ARTICLE_LIST";
    public static final String EXTRA_ARTICLE_ITEM_POSITION = "com.example.eric.quickheadline.EXTRA_ARTICLE_ITEM_POSITION";
    public static final String EXTRA_WEB_VIEW_URL = "com.example.eric.quickheadline.EXTRA_WEB_VIEW_URL";
    public static final String ARGUMENT_ARTICLE_POSITION = "com.example.eric.quickheadline.ARGUMENT_ARTICLE_POSITION";
    public static final String ARGUMENT_ARTICLE_ITEM = "com.example.eric.quickheadline.ARGUMENT_ARTICLE_ITEM";
    public static final String ARGUMENT_ARTICLE_LIST = "com.example.eric.quickheadline.ARGUMENT_ARTICLE_LIST";

    /*constants used for Shard Preference*/
    public static final String PREF_IS_FIRST_TIME_LAUNCH = "pref_is_first_time_launch";
    public static final String PREF_IS_FIRST_TIME_CHECKED = "PREF_IS_FIRST_TIME_CHECKED";
    public static final String PREF_CALLBACK = "PREF_CALLBACK";
    public static final String PREF_LAST_NOTIFICATION = "PREF_LAST_NOTIFICATION";
    public static final String PREF_ARTICLE_TEXT_SIZE = "key_article_text_size";
    public static final String PREF_TEMPERATURE_UNITS = "key_temperature_units";
    public static final String PREF_ENABLE_NOTIFICATIONS = "key_enable_notifications";

    /*constants representing base urls*/
    public static final String GEO_NAME_BASE_URL = "http://api.geonames.org/";
    public static final String NEWS_BASE_URL = "https://newsapi.org/v2/";
    public static final String MAP_BASE_URL = "https://maps.googleapis.com/maps/api/";
    public static final String WEATHER_BASE_URL = "https://api.darksky.net/";

    /*used as static query parameters in API endpoints*/
    public static final String QUERY_PARAMS_EXCLUDE = "[minutely,hourly,flags]";
    public static final int QUERY_PARAMS_PAGE_SIZE = 100;
    public static final String QUERY_PARAMS_USER_NAME = "ericafenyo";

    /* Serves as a file name for File IOStream operation*/
    public static final String FILE_IO_NAME = "paramData.json";


}
