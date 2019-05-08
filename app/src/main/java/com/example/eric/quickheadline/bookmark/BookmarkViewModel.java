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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.Callback;
import com.example.eric.quickheadline.model.Bookmark;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by eric on 16/02/2018.
 * an Android Arch. ViewModel class that
 * prepares the observable bookmarks gotten from {@link BookmarkRepository#performQuery(BookmarkDb)} by implementing
 * a callback for an observer
 */

public class BookmarkViewModel extends AndroidViewModel implements Callback {
    private BookmarkRepository mRepository;
    private LiveData<List<Bookmark>> mBookmark;

    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookmarkRepositoryImpl(this);
    }

    public LiveData<List<Bookmark>> getBookmark() {
        loadData();
        return this.mBookmark;
    }

    private void loadData() {
        try {
            mRepository.executeSyncTask(BookmarkDb.getINSTANCE(this.getApplication()));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setBookmark(LiveData<List<Bookmark>> mBookmark) {
        this.mBookmark = mBookmark;
    }

    @Override
    public void onSuccess(@Nullable Object bookmarks) {
        //this is the method called after a successful bookmark query

        setBookmark((LiveData<List<Bookmark>>) bookmarks);
    }

    @Override
    public void onFailure(@Nullable Object error) {
        //not implemented
    }

    @Override
    public void onEmpty() {
        //not implemented
    }

    @Override
    public void onNull() {
        //not implemented
    }
}

