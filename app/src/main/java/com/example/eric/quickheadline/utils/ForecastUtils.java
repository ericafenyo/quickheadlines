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

package com.example.eric.quickheadline.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eric.quickheadline.R;

/**
 * Created by eric on 12/03/2018.
 */

public class ForecastUtils {
    private static final String TAG = ForecastUtils.class.getName();

    /**
     * returns drawable resource id based on an icon weather condition
     */
    public static int getDrawableForWeatherCondition(@NonNull Context context, String iconCondition) {
        if (iconCondition != null && iconCondition.length() != 0) {
            if (iconCondition.equals(context.getString(R.string.icon_condition_01))) {
                return R.drawable.ic_clear;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_02))) {
                return R.drawable.ic_nt_clear;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_03))) {
                return R.drawable.ic_rain;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_04))) {
                return R.drawable.ic_snow;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_05))) {
                return R.drawable.ic_sleet;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_06))) {
                return R.drawable.ic_wind;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_07))) {
                return R.drawable.ic_fog;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_08))) {
                return R.drawable.ic_cloudy;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_09))) {
                return R.drawable.ic_partly_cloudy;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_10))) {
                return R.drawable.ic_nt_partly_cloudy;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_11))) {
                return R.drawable.ic_sleet;
            } else if (iconCondition.equals(context.getString(R.string.icon_condition_12))) {
                return R.drawable.ic_thunder_storms;
            }
        }
        Log.e(TAG, "Unknown Weather Condition: " + iconCondition);
        return R.drawable.ic_clear;
    }


    public static double convertFahrenheitToCelsius(double tempFahrenheit) {
        return (tempFahrenheit - 32) / 1.8;
    }

    public static double convertMillibarsToKiloPascale(double pressureMillibar) {
        return pressureMillibar * 0.1;
    }

    public static double doubleToPercentage(double value) {
        return value * 100;
    }

}
