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

package com.ericafenyo.quickheadline.discover;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

/**
 * Created by eric on 13/03/2018.
 * a {@link ViewModelProvider.Factory} class use to construct an
 * Android arch. ViewModel with custom constructor
 * here my second custom argument is a string
 */

public class DiscoverViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application mApplication;
    private String mCategory;


    public DiscoverViewModelFactory(@NonNull Application mApplication, String mCategory) {
        this.mApplication = mApplication;
        this.mCategory = mCategory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DiscoverViewModel.class)) {
            //noinspection unchecked
            return (T) new DiscoverViewModel(mApplication, mCategory);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
