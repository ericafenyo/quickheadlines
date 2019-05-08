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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import static com.example.eric.quickheadline.utils.ConstantFields.PREF_CALLBACK;
import static com.example.eric.quickheadline.utils.ConstantFields.PREF_IS_FIRST_TIME_CHECKED;
import static com.example.eric.quickheadline.utils.ConstantFields.PREF_IS_FIRST_TIME_LAUNCH;


/**
 * Created by eric on 20/02/2018.
 * this class handles the apps sharedPreference methods
 */

public class PreferenceUtils {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    @SuppressLint("CommitPrefEdits")
    public PreferenceUtils(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
        mEditor = mPreferences.edit();
    }

    public boolean isFirstTimeLaunch() {
        final boolean defValue = true;
        return mPreferences.getBoolean(PREF_IS_FIRST_TIME_LAUNCH, defValue);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        mEditor.putBoolean(PREF_IS_FIRST_TIME_LAUNCH, isFirstTime);
        mEditor.commit();
    }

    public boolean isFirstTimeChecked() {
        final boolean defValue = true;
        return mPreferences.getBoolean(PREF_IS_FIRST_TIME_CHECKED, defValue);
    }

    public void setFirstTimeChecked(boolean isBookmarked) {
        mEditor.putBoolean(PREF_IS_FIRST_TIME_CHECKED, isBookmarked);
        mEditor.commit();
    }

    public boolean isBookmarked(String PREF_IS_BOOKMARKED) {
        final boolean defValue = false;
        return mPreferences.getBoolean(PREF_IS_BOOKMARKED, defValue);
    }

    public void setIsBookmarked(String PREF_IS_BOOKMARKED, boolean isBookmarked) {
        mEditor.putBoolean(PREF_IS_BOOKMARKED, isBookmarked);
        mEditor.commit();
    }


    public String getCallback() {
        String defValue = "onEmpty";
        return mPreferences.getString(PREF_CALLBACK, defValue);
    }

    public void setCallback(String callback) {
        mEditor.putString(PREF_CALLBACK, callback);
        mEditor.commit();
    }


    public String getTemperatureUnitId() {
        final String defValue = "0";
        return mPreferences.getString(ConstantFields.PREF_TEMPERATURE_UNITS, defValue);
    }

    public boolean isNotificationEnabled() {
        final boolean defValue = true;
        return mPreferences.getBoolean(ConstantFields.PREF_ENABLE_NOTIFICATIONS, defValue);
    }

    public String getArticleTextSize() {
        final String defValue = "20";
        return mPreferences.getString(ConstantFields.PREF_ARTICLE_TEXT_SIZE, defValue);
    }


    public void saveLastNotificationTime(long timeOfNotification) {
        mEditor.putLong(ConstantFields.PREF_LAST_NOTIFICATION, timeOfNotification);
        mEditor.commit();
    }

    public long getLastNotificationTimeInMillis() {
        return mPreferences.getLong(ConstantFields.PREF_LAST_NOTIFICATION, 0);
    }

    public long getElapsedTimeSinceLastNotification() {
        long lastNotificationTimeMillis = getLastNotificationTimeInMillis();
        return System.currentTimeMillis() - lastNotificationTimeMillis;
    }
}