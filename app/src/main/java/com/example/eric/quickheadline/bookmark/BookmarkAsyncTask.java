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

/**
 * Created by eric on 16/02/2018.
 * AsyncTask class that provides background thread to perform database query
 */

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.eric.quickheadline.Callback;
import com.example.eric.quickheadline.model.Bookmark;

import java.util.List;

public class BookmarkAsyncTask extends AsyncTask<Void, Void, LiveData<List<Bookmark>>> {

    private final BookmarkDb db;
    private Callback mCallback;

    public BookmarkAsyncTask(BookmarkDb db, Callback mCallback) {
        this.db = db;
        this.mCallback = mCallback;
    }

    @Override
    protected LiveData<List<Bookmark>> doInBackground(Void... params) {
        return new BookmarkRepositoryImpl(mCallback).performQuery(db);
    }
}
