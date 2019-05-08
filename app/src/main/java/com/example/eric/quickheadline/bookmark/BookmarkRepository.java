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


import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.model.Bookmark;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by eric on 15/02/2018.
 * a Repository class for out bookmark database transactions
 */

public interface BookmarkRepository {

    /**
     * perform the database query here
     *
     * @param db the database {@link BookmarkDb} from which bookmark data can be access
     * @return an observable list of bookmarks
     */
    LiveData<List<Bookmark>> performQuery(@Nullable BookmarkDb db);

    /**
     * execute an async task method
     *
     * @param db the database {@link BookmarkDb} from which we can access bookmark data
     */
    void executeSyncTask(@Nullable final BookmarkDb db) throws ExecutionException, InterruptedException;

}


