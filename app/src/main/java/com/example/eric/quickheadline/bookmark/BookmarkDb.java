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

package com.example.eric.quickheadline.bookmark;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.model.Bookmark;


/**
 * Created by eric on 15/02/2018.
 * bookmarks database class using Room library from Android Architecture Components
 */

@Database(entities = Bookmark.class, version = 1, exportSchema = false)
public abstract class BookmarkDb extends RoomDatabase {
    public abstract BookmarkDao bookmarkDao();

    private static BookmarkDb INSTANCE;

    public static BookmarkDb getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookmarkDb.class,
                    context.getString(R.string.bookmark_db_name))
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
