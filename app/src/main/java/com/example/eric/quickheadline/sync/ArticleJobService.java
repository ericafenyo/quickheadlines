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

package com.example.eric.quickheadline.sync;

import android.util.Log;

import com.example.eric.quickheadline.Callback;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by eric on 14/02/2018.
 * this service class is triggered at regular intervals by a Job scheduler
 * in order to sync data between our local database and the source
 */

public class ArticleJobService extends JobService {
    private static final String TAG = ArticleJobService.class.getName();

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(TAG, "ArticleJobService");
        ArticleSyncTask.execute(getApplication(), new Callback() {
            @Override
            public void onSuccess(Object data) {
                Log.v(TAG, "onSuccess()");
            }

            @Override
            public void onFailure(Object error) {
                Log.e(TAG, "onFailure()");
            }

            @Override
            public void onEmpty() {
                Log.v(TAG, "onEmpty()");
            }

            @Override
            public void onNull() {
                Log.v(TAG, "onNull()");
            }
        });
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

}
