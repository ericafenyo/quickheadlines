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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.home.MainActivity;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import javax.inject.Inject;

/**
 * a simple splash activity that either launches the {@link WelcomeActivity} or the {@link MainActivity}
 * base on whether it is the first time the app was launched or not
 */
public class SplashActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashActivity.class.getName();//for debugging purpose

    @Inject
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((MyApp) getApplication()).getComponent().inject(this);
        Log.v(LOG_TAG, "SplashActivity");

        //check if the app is launched for the first time and display the correct screen
        if (preferenceUtils.isFirstTimeLaunch()) {
            Log.v(LOG_TAG, "onCreate()");
            launchWelcomeScreen();
        } else {
            launchHomeScreen();
        }
    }

    ////////// Inner Methods ////////////////////

    private void launchWelcomeScreen() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

