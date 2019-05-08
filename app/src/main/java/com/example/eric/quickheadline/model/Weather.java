
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

package com.example.eric.quickheadline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * simple POJO class for the Weather Endpoint
 * 1.{@link Weather}
 * 2.{@link Currently}
 */
public class Weather {

    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("currently")
    @Expose
    private Currently currently;

    ////////// Currently Getters ////////////////////

    public String getTimezone() {
        return timezone;
    }

    public Currently getCurrently() {
        return currently;
    }


    public static class Currently {

        @SerializedName("time")
        @Expose
        private long time;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("temperature")
        @Expose
        private double temperature;
        @SerializedName("apparentTemperature")
        @Expose
        private double apparentTemperature;
        @SerializedName("humidity")
        @Expose
        private double humidity;
        @SerializedName("pressure")
        @Expose
        private double pressure;
        @SerializedName("windSpeed")
        @Expose
        private double windSpeed;
        @SerializedName("visibility")
        @Expose
        private double visibility;

        ////////// Currently Getters ////////////////////
        public long getTime() {
            return time;
        }

        public String getSummary() {
            return summary;
        }

        public String getIcon() {
            return icon;
        }

        public double getTemperature() {
            return temperature;
        }

        public double getApparentTemperature() {
            return apparentTemperature;
        }

        public double getHumidity() {
            return humidity;
        }

        public double getPressure() {
            return pressure;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public double getVisibility() {
            return visibility;
        }
    }
}
