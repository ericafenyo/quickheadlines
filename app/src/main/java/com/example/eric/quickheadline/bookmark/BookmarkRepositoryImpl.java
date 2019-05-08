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
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.quickheadline.Callback;
import com.example.eric.quickheadline.model.Bookmark;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by eric on 08/03/2018.
 * from this class we call methods that perform database transactions
 * such use delete and insert
 * <p>
 * the "performQuery() " method is executed in an {@link BookmarkAsyncTask} and the data
 * passed to {@link BookmarkViewModel} through a callback interface{@link Callback}
 */

public class BookmarkRepositoryImpl implements BookmarkRepository {
    private static final String LOG_TAG = BookmarkRepositoryImpl.class.getName();//for debugging purpose
    private Callback mCallback;

    public BookmarkRepositoryImpl(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public static int performDelete(@NonNull final BookmarkDb db, Bookmark bookmark) {
        return db.bookmarkDao().delete(bookmark);
    }

    public static long[] performInsert(@NonNull BookmarkDb db, Bookmark bookmark) {
        return db.bookmarkDao().insert(bookmark);
    }

    @Override
    public LiveData<List<Bookmark>> performQuery(@NonNull BookmarkDb db) {
        LiveData<List<Bookmark>> bookmarks = db.bookmarkDao().queryAll();
        mCallback.onSuccess(bookmarks);
        if (bookmarks == null) {
            Log.e(LOG_TAG, "Bookmark is null");
        }
        return bookmarks;
    }

    @Override
    public void executeSyncTask(@NonNull final BookmarkDb db) throws ExecutionException, InterruptedException {
        BookmarkAsyncTask bookmarkSync = new BookmarkAsyncTask(db, mCallback);
        //wait for the data
        while (bookmarkSync.execute().get() == null) {
            bookmarkSync.wait(500);
        }

    }

}
