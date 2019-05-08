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

package com.example.eric.quickheadline.home;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.quickheadline.MapsActivity;
import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.utils.DateUtils;
import com.example.eric.quickheadline.utils.ForecastUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_WEATHER_LOADER = 656;
    private PreferenceUtils preferenceUtils;

    @BindView(R.id.tv_summary)
    TextView tvSummary;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_apparent_temperature)
    TextView tvRealFeel;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.tv_visibility)
    TextView tvVisibility;
    @BindView(R.id.tv_wind_speed)
    TextView tvWindSpeed;
    @BindView(R.id.tv_date_current_date)
    TextView tvDateCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Explode animation
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
        setContentView(R.layout.activity_weather_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar_weather);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setTitle("Weather forecast");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferenceUtils = ((MyApp) getApplication()).getComponent().getPreferenceUtils();

        getSupportLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_view_map:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_WEATHER_LOADER:

                return new CursorLoader(this, WeatherEntry.CONTENT_URI_WEATHER, null, null, null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        boolean isValidCursor = false;
        /*Id used to format temperature unit*/
        String unitId = preferenceUtils.getTemperatureUnitId();

        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            isValidCursor = true;
//            hideLoading();
        }
//
        if (!isValidCursor) {
            /* No data to display, simply return and do nothing */
            return;
        }

        data.moveToFirst();
                 /* database table column indices for weather information */
        int index_summary = data.getColumnIndex(WeatherEntry.COLUMN_NAME_SUMMARY);
        int index_temperature = data.getColumnIndex(WeatherEntry.COLUMN_NAME_TEMPERATURE);
        int index_apparent_temperature = data.getColumnIndex(WeatherEntry.COLUMN_NAME_APPARENT_TEMPERATURE);
        int index_humidity = data.getColumnIndex(WeatherEntry.COLUMN_NAME_HUMIDITY);
        int index_visibility = data.getColumnIndex(WeatherEntry.COLUMN_NAME_VISIBILITY);
        int index_wind_speed = data.getColumnIndex(WeatherEntry.COLUMN_NAME_WIND_SPEED);
        int index_pressure = data.getColumnIndex(WeatherEntry.COLUMN_NAME_PRESSURE);
        int index_icon = data.getColumnIndex(WeatherEntry.COLUMN_NAME_ICON);

        //set normal and realFeel temperature
        double temperature = data.getDouble(index_temperature);
        double apparentTemperature = data.getDouble(index_apparent_temperature);
        tvTemperature.setText(formatTemperature(unitId, temperature));
        tvRealFeel.setText(getString(R.string.format_realFeel, formatTemperature(unitId, apparentTemperature)));

        //setup forecast summary
        String summary = data.getString(index_summary);
        tvSummary.setText(summary);

        //set current date
        long millisStart = Calendar.getInstance().getTimeInMillis();
        String unixDate = DateUtils.getUNIXDate(millisStart);
        tvDateCurrentDate.setText(unixDate);

        //format and set pressure  in kilo Pascale
        double pressure = ForecastUtils.convertMillibarsToKiloPascale(data.getDouble(index_pressure));
        String formattedPressure = String.format(Locale.getDefault(), getString(R.string.format_pressure), pressure);
        tvPressure.setText(formattedPressure);

        //format and set visibility in in miles
        double visibility = data.getDouble(index_visibility);
        String formattedVisibility = String.format(Locale.getDefault(), getString(R.string.format_visibility), visibility);
        tvVisibility.setText(formattedVisibility);

        //format and set wind speed in miles per hour.
        double windSpeed = data.getDouble(index_wind_speed);
        String formattedWindSpeed = String.format(Locale.getDefault(), getString(R.string.format_wind_speed), windSpeed);
        tvWindSpeed.setText(formattedWindSpeed);

        // format and set relative humidity between 0 and 1, inclusive in percentage
        double humidity = ForecastUtils.doubleToPercentage(data.getDouble(index_humidity));
        String formattedHumidity = String.format(Locale.getDefault(), getString(R.string.format_humidity), humidity);
        tvHumidity.setText(formattedHumidity);

        //set weather condition icon
        String icon = data.getString(index_icon);
        if (icon != null && icon.length() != 0) {
            //noinspection ConstantConditions
            Drawable drawable = ContextCompat.getDrawable(this, ForecastUtils.getDrawableForWeatherCondition(this, icon));
            GlideApp.with(this).load(drawable).into(ivIcon);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * removes decimals and append appropriate units to temperature values
     *
     * @param unitId         a shardPreference value that determines which temperature unit to use
     *                       users can change this value in the apps settings
     * @param tempFahrenheit temperature in degrees Fahrenheit (°F)
     * @return formatted unit-specific temperature
     */
    private String formatTemperature(String unitId, double tempFahrenheit) {
        String fahrenheitUnitId = "1";
        String celsiusUnitId = "0";

        //To Fahrenheit
        if (unitId.equals(fahrenheitUnitId)) {
            tvUnit.setText(R.string.fahrenheit_unit);
            return String.format(Locale.getDefault(), getString(R.string.format_temperature_fahrenheit), tempFahrenheit);

        }  //To Celsius
        else if (unitId.equals(celsiusUnitId)) {
            tvUnit.setText(R.string.celsius_unit);
            double tempCelsius = ForecastUtils.convertFahrenheitToCelsius(tempFahrenheit);
            return String.format(Locale.getDefault(), getString(R.string.format_temperature_celsius), tempCelsius);
        } //Default
        else {
            tvUnit.setText(R.string.fahrenheit_unit);
            return String.format(Locale.getDefault(), getString(R.string.format_temperature_fahrenheit), tempFahrenheit);
        }
    }

}
