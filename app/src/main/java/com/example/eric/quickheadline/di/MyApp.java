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

package com.example.eric.quickheadline.di;

import android.app.Application;


/**
 * Created by eric on 05/02/2018.
 * Application class to instantiate dagger component
 */

public class MyApp extends Application {
    private ModuleComponent mModuleComponent;

    /*Getter*/
    public ModuleComponent getComponent() {
        return mModuleComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mModuleComponent = DaggerModuleComponent.builder()
                .appModule(new AppModule(this))
                .moduleProvider(new ModuleProvider())
                .build();
    }


}
