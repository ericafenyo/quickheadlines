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

package com.example.javautil;


public class StringUtils {

    public static String getLatLng(String rawLatLng) {
        String sub = null;
        if (rawLatLng != null) {
            sub = rawLatLng.substring(10, rawLatLng.length() - 1);
        }
        return sub;
    }

    public static String getLng(String rawLatLng) {
        int secondIndex = 1;
        String[] latLngArr = getLatLngArr(getLatLng(rawLatLng));
        return latLngArr[secondIndex];
    }

    public static String getLat(String rawLatLng) {
        final int firstIndex = 0;
        String[] latLngArr = getLatLngArr(getLatLng(rawLatLng));
        return latLngArr[firstIndex];
    }

    public static String[] getLatLngArr(String splitLatLng) {
        return splitLatLng.split(",");
    }

    public static String getLanguage(String language) {
        if (language != null && language.length() != 0) {
            return language.substring(0, 2);
        }
        return null;
    }
}
