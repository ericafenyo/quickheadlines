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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.eric.quickheadline.di.ApiEndpoint;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.home.MainActivity;
import com.example.eric.quickheadline.model.Country;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.JsonUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;
import com.example.javautil.StringUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.eric.quickheadline.utils.ConstantFields.FILE_IO_NAME;
import static com.example.eric.quickheadline.utils.ConstantFields.QUERY_PARAMS_USER_NAME;

/**
 * this class handles the place picker api and store the responses in a local file
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final String LOG_TAG = WelcomeActivity.class.getName();//for debugging purpose
    private static int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.tv_terms)
    TextView tvTerms;

    @Inject
    PreferenceUtils preferenceUtils;

    @Inject
    @Named("geonames")
    ApiEndpoint geoNamesEndpoint;

    @Named("article")
    @Inject
    ApiEndpoint endpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        ((MyApp) getApplication()).getComponent().inject(this);

        Log.v(LOG_TAG, "WelcomeActivity");
        //makes links within TextViews clickable
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                String lat = StringUtils.getLat(place.getLatLng().toString());
                String lng = StringUtils.getLng(place.getLatLng().toString());

                geoNamesEndpoint.getCountry(lat, lng, QUERY_PARAMS_USER_NAME).enqueue(new Callback<Country>() {
                    @Override
                    public void onResponse(Call<Country> call, Response<Country> response) {
                        if (response.body() != null) {
                            Country data = response.body();
                            Gson gson = new Gson();
                            //retrieves json objects
                            String countryCode = data.getCountryCode();
                            String languages = StringUtils.getLanguage(data.getLanguages());
                            String countryName = data.getCountryName();
//                            Log.v("LOG_TAG", String.valueOf(place.getAddress()));//for debugging purpose
//                            Log.v("LOG_TAG", String.valueOf(place.getLocale()));//for debugging purpose
//                            Log.v("LOG_TAG", String.valueOf(place.getName()));//for debugging purpose

                            /* store objects in array to be saved to file
                            * [
                            * 0 : "lat",
                            * 1 : "lng",
                            * 2 : "countryCode",
                            * 3 : "languages",
                            * 4 : "countryName"
                            * ]
                            * */
                            String[] arr = {lat, lng, countryCode, languages, countryName};
                            String stringArr = gson.toJson(arr);
                            try {
                                JsonUtils.writeFile(WelcomeActivity.this, FILE_IO_NAME, stringArr);
                                performCountrySupportVerification(countryCode, countryName);
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Failed to write data " + e);
                            }

                        } else {
                            Log.e(LOG_TAG, "response is null");
                        }
                    }

                    @Override
                    public void onFailure(Call<Country> call, Throwable t) {
                        Log.e(LOG_TAG, "Failed to load geoName data " + t);
                    }
                });
            }
        }
    }


    /**
     * called on click of the Continue button
     *
     * @param view view
     */
    public void onContinue(View view) {

        if (HelperUtils.hasInternetConnectivity(this)) {
            launchPlacePicker();
        } else {
            HelperUtils.toast(getApplicationContext(),getString(R.string.error_no_internet));
        }
    }

    ////////// Inner Methods ////////////////////

    /**
     * launches the built-in place picker widget
     */
    private void launchPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * verify if the country is supported before loading the full data
     * we make a call to the API end point which returns one json object
     * if it has been successful we proceed to launching the home screen and load full data
     */
    private void performCountrySupportVerification(String countryCode, String countryName) {

        int numberOfResults = 1;
        endpoint.getArticle(countryCode, numberOfResults, BuildConfig.NEWS_API_KEY).enqueue(new retrofit2.Callback<News>() {

            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                List<News.Article> articles;

                if (response.body() != null) {
                    articles = response.body().getArticles();
                    if (articles.size() == 0) {
                        onEmpty(countryName);
                    } else {
                        onSuccess();
                        Log.e(LOG_TAG, articles.toString());
                    }

                } else {
                    Log.e(LOG_TAG, "response is null");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(LOG_TAG, getString(R.string.error_on_network_call) + t);
            }
        });
    }


    private void onSuccess() {
        preferenceUtils.setFirstTimeLaunch(false);
        launchHomeScreen();
        Log.v(LOG_TAG, "onSuccess()");
    }

    private void onEmpty(String location) {
        launchPlacePicker();
        Log.v(LOG_TAG, "onEmpty()");
        HelperUtils.toast(getApplicationContext(), getString(R.string.format_country_not_supported,location));
    }
}



