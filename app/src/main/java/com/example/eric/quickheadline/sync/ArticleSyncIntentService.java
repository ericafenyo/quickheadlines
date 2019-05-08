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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.Callback;


/**
 * Created by eric on 04/03/2018.
 * a service to quickly execute {@link ArticleSyncTask#execute(Context, Callback)}
 */

public class ArticleSyncIntentService extends IntentService {

//    private static final String LOG_TAG = ArticleSyncIntentService.class.getName();//for debugging purpose

    public ArticleSyncIntentService() {
        super("ArticleSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArticleSyncTask.execute(getApplication(), new Callback() {
            @Override
            public void onSuccess(Object data) {
//               Log.v(LOG_TAG,"onSuccess()");//for debugging purpose
            }

            @Override
            public void onFailure(Object error) {
//               Log.v(LOG_TAG,"onFailure()");//for debugging purpose
            }

            @Override
            public void onEmpty() {
//              Log.v(LOG_TAG,"onEmpty()");//for debugging purpose
            }

            @Override
            public void onNull() {
//              Log.v(LOG_TAG,"onNull()");//for debugging purpose
            }
        });
    }
}
