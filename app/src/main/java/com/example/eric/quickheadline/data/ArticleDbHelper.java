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

package com.example.eric.quickheadline.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eric.quickheadline.contract.ArticleEntry;


/**
 * Created by eric on 07/02/2018.
 * a simple class for SQLite database and table creation
 */

public class ArticleDbHelper extends SQLiteOpenHelper {

    /* This is the name of our article database */
    public static final String DATABASE_NAME = "article.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 1;

    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createArticleTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * @return a simple SQL statement that will create a table
     */
    private String createArticleTable() {

        return "CREATE TABLE " +

                ArticleEntry.TABLE_NAME +

                " (" +

                ArticleEntry._ID + " INTEGER PRIMARY KEY, " +

                ArticleEntry.COLUMN_NAME_SOURCE + " TEXT," +

                ArticleEntry.COLUMN_NAME_AUTHOR + " TEXT," +

                ArticleEntry.COLUMN_NAME_TITLE + " TEXT," +

                ArticleEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +

                ArticleEntry.COLUMN_NAME_URL + " TEXT, " +

                ArticleEntry.COLUMN_NAME_URL_TO_IMAGE + " TEXT, " +

                ArticleEntry.COLUMN_NAME_PUBLISHED_DATE + " TEXT " +

                ")";
    }
}
