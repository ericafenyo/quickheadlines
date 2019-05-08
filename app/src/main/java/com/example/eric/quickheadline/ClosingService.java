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

package com.example.eric.quickheadline;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.utils.PreferenceUtils;

/**
 * Created by eric on 16/03/2018.
 * used to perform sharedPreference task when the app is closed from recent apps and the {@link Activity#onDestroy()}
 * method is not called
 */

public class ClosingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        PreferenceUtils preferenceUtils = ((MyApp) getApplication()).getComponent().getPreferenceUtils();
        preferenceUtils.setFirstTimeChecked(true);
        stopSelf();
    }
}
