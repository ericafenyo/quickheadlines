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

package com.example.eric.quickheadline.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.data.WeatherDbHelper;

/**
 * Created by eric on 08/02/2018.
 * <p>
 * This class serves as the ContentProvider for our weather. It allows us to bulkInsert
 * data, query all data
 */

public class WeatherProvider extends ContentProvider {

    private static final int CODE_CURRENT_WEATHER = 200;

    /*The URI Matcher used by this content provider.*/
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        /*
         * 1. UriMatcher matcher
         * 2. CONTENT_AUTHORITY :String (package name)
         */

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherEntry.WEATHER_CONTENT_AUTHORITY;

        /* This URI is content://com.example.eric.quickheadline/currently */
        matcher.addURI(authority, WeatherEntry.PATH_CURRENT_WEATHER, CODE_CURRENT_WEATHER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_CURRENT_WEATHER:
                cursor = db.query(WeatherEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_CURRENT_WEATHER:
                Uri returnedUri;

                long _id = db.insertWithOnConflict(WeatherEntry.TABLE_NAME, null, values, SQLiteDatabase
                        .CONFLICT_REPLACE);
                if (_id != -1) {
                    returnedUri = ContentUris.withAppendedId(uri, _id);

                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnedUri;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        /*number of rows deleted*/
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_CURRENT_WEATHER:
                numRowsDeleted = db.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String table = WeatherEntry.TABLE_NAME;

        switch (sUriMatcher.match(uri)) {
            case CODE_CURRENT_WEATHER:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(table, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                    getContext().getContentResolver().notifyChange(uri, null);
                } finally {
                    db.endTransaction();
                }
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
