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

package com.ericafenyo.quickheadline.bookmark;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ericafenyo.quickheadline.model.Bookmark;

import java.util.List;

/**
 * Created by eric on 15/02/2018.
 * a Data access object class for bookmarks entry
 */

@Dao
public interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    LiveData<List<Bookmark>> queryAll();

    @Insert()
    long[] insert(Bookmark... bookmarks);

    @Delete()
    int delete(Bookmark bookmark);
}
