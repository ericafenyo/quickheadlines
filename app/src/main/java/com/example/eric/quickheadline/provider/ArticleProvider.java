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
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.contract.ArticleEntry;
import com.example.eric.quickheadline.data.ArticleDbHelper;


/**
 * Created by eric on 08/02/2018.
 * This class serves as the ContentProvider for the latest news article. It allows us to bulkInsert
 * data, query all data, and delete data
 */


public class ArticleProvider extends ContentProvider {

    private static final int CODE_LATEST_ARTICLE = 100;

    /*The URI Matcher used by this content provider.*/
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ArticleDbHelper mOpenHelper;

    /**
     * @return A UriMatcher that correctly matches the constant for CODE_LATEST_ARTICLE
     */
    private static UriMatcher buildUriMatcher() {

        /*
         * 1. UriMatcher matcher
         * 2. CONTENT_AUTHORITY :String (package name + className)
         */

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ArticleEntry.ARTICLE_CONTENT_AUTHORITY;

        /* This URI is content://com.example.eric.quickheadline/article */
        matcher.addURI(authority, ArticleEntry.PATH_ARTICLE, CODE_LATEST_ARTICLE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ArticleDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_LATEST_ARTICLE:
                cursor = db.query(ArticleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        /*number of rows deleted*/
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_LATEST_ARTICLE:
                numRowsDeleted = db.delete(ArticleEntry.TABLE_NAME, selection, selectionArgs);
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
        switch (sUriMatcher.match(uri)) {
            case CODE_LATEST_ARTICLE:
                db.beginTransaction();
                int rowsInserted = 0;

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ArticleEntry.TABLE_NAME, null, value);
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