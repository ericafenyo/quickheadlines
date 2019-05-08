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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.SettingsActivity;
import com.example.eric.quickheadline.contract.ArticleEntry;
import com.example.eric.quickheadline.contract.WeatherEntry;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.ForecastUtils;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;
import com.example.eric.quickheadline.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;

/**
 * A simple {@link Fragment} for displaying top article stories
 */
public class HomeFragment extends Fragment implements ArticleAdapter.onItemSelected {
    private static final int ID_ARTICLE_LOADER = 250;
    private static final int ID_WEATHER_LOADER = 856;
    private static final String LOG_TAG = HomeFragment.class.getName();//for debugging purpose

    private ArticleAdapter mAdapter;

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_home)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar_home)
    ProgressBar progressBar;
    @BindView(R.id.weather_card)
    CardView weatherCard;

    /*weather fields*/
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
    private String unitId;

    public HomeFragment() {
        //Empty public constructor
    }
    
    @Inject
    PreferenceUtils preferenceUtils;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* Inflate the layout for this fragment and setup ButterKnife*/
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        ((MyApp) getActivity().getApplication()).getComponent().inject(this);

        unitId = preferenceUtils.getTemperatureUnitId();
        mAdapter = new ArticleAdapter(getActivity(), this);
        //noinspection ConstantConditions
        getActivity().getSupportLoaderManager().initLoader(ID_ARTICLE_LOADER, null, loaderArticle);
        getActivity().getSupportLoaderManager().initLoader(ID_WEATHER_LOADER, null, loaderWeather);

        weatherCard.setOnClickListener(new onClickImpl());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_round_menu_24px));
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_top_stories);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        //perform initialization
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //set search hint message and listener
        searchView.setQueryHint(getResources().getString(R.string.search_hint_text));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //noinspection ConstantConditions
                HelperUtils.query(getActivity().getApplication(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    ////////// ArticleAdapter#onItemSelected Implementations ////////////////////

    @Override
    public void onClick(int position, List<News.Article> articles) {
        String fragTAG = ArticlePagerFragment.class.getName();
        Fragment fragment = ArticlePagerFragment.newInstance(articles, position);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frame_container_main, fragment, fragment.getTag())
                .addToBackStack(fragTAG)
                .commit();
    }

    @Override
    public void onLongClick(Bookmark bookmark) {

    }

    ////////// Article CursorLoader Callbacks ////////////////////

    /**
     * Cursor loader for querying News Article from out database
     */
    private LoaderManager.LoaderCallbacks<Cursor> loaderArticle = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

            switch (loaderId) {
                case ID_ARTICLE_LOADER:

                    //noinspection ConstantConditions
                    return new CursorLoader(getActivity(), ArticleEntry.CONTENT_URI_ARTICLE, null, null, null,
                            _ID);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            boolean isValidArticleCursor = false;


            if (data != null && data.moveToFirst()) {
            /* the Cursor is valid*/
                isValidArticleCursor = true;
                HelperUtils.hideLoading(recyclerView, progressBar);
            }

            if (!isValidArticleCursor) {
            /* No data to display, simply return and do nothing */
                return;
            }

            List<News.Article> articles = new ArrayList<>();
            while (data.moveToNext()) {

                 /* database table column indices for article information */
                int index_source = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_SOURCE);
                int index_author = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_AUTHOR);
                int index_title = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_TITLE);
                int index_description = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_DESCRIPTION);
                int index_url = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_URL);
                int index_urlToImage = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_URL_TO_IMAGE);

                int index_publishedDate = data.getColumnIndex(ArticleEntry
                        .COLUMN_NAME_PUBLISHED_DATE);

                 /* Read the article data from the cursor */
                String source = data.getString(index_source);
                String author = data.getString(index_author);
                String description = data.getString(index_description);
                String title = data.getString(index_title);
                String url = data.getString(index_url);
                String urlToImage = data.getString(index_urlToImage);
                String publishedDate = data.getString(index_publishedDate);

                //constructs an Article list object
                articles.add(new News.Article(new News.Source(source), author, title,
                        description, url, urlToImage, publishedDate));

                populateRecyclerView(articles);
            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }
    };

    ////////// Weather CursorLoader Callbacks ////////////////////

    /**
     * Cursor loader for querying Weather information from out database
     */
    LoaderManager.LoaderCallbacks<Cursor> loaderWeather = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {

            String[] projection = {
                    WeatherEntry.COLUMN_NAME_SUMMARY,
                    WeatherEntry.COLUMN_NAME_TEMPERATURE,
                    WeatherEntry.COLUMN_NAME_APPARENT_TEMPERATURE,
                    WeatherEntry.COLUMN_NAME_ICON
            };
            switch (loaderId) {
                case ID_WEATHER_LOADER:
                    return new CursorLoader(getActivity(), WeatherEntry.CONTENT_URI_WEATHER, projection, null, null,
                            null);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            boolean isValidCursor = false;

            if (isAdded()) {
                if (data != null && data.moveToFirst()) {
                    // We have valid data, continue on to bind the data to the UI
                    isValidCursor = true;
                }
                if (!isValidCursor) {
                    //No data to display, simply return and do nothing
                    return;
                }

                data.moveToFirst();
                // database table column indices for weather information
                int index_summary = data.getColumnIndex(WeatherEntry.COLUMN_NAME_SUMMARY);
                int index_temperature = data.getColumnIndex(WeatherEntry.COLUMN_NAME_TEMPERATURE);
                int index_apparent_temperature = data.getColumnIndex(WeatherEntry.COLUMN_NAME_APPARENT_TEMPERATURE);
                int index_icon = data.getColumnIndex(WeatherEntry.COLUMN_NAME_ICON);

                //set normal and realFeel temperature
                double temperature = data.getDouble(index_temperature);
                double apparentTemperature = data.getDouble(index_apparent_temperature);
                tvTemperature.setText(formatTemperature(unitId, temperature));
                tvRealFeel.setText(getString(R.string.format_realFeel, formatTemperature(unitId, apparentTemperature)));

                //setup forecast summary
                String summary = data.getString(index_summary);
                tvSummary.setText(summary);

                //Setup weather condition icon
                String icon = data.getString(index_icon);
                if (icon != null && icon.length() != 0) {
                    //noinspection ConstantConditions
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), ForecastUtils.getDrawableForWeatherCondition(getActivity(), icon));
                    GlideApp.with(getActivity()).load(drawable).into(ivIcon);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };


    ////////// Inner Methods ////////////////////


    /**
     * formats and add appropriate units to temperature values
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

    private void populateRecyclerView(List<News.Article> data) {
        if (data != null) {
            mAdapter.setData(data);
            if (HelperUtils.isLandscapeMode(getActivity())) {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            } else if (HelperUtils.isPortraitMode(getActivity())) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            recyclerView.setAdapter(mAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
        } else {
            return;
        }
    }

    /**
     * launch a detailed view for the weather information
     */
    private class onClickImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WeatherDetailActivity.class);
            Pair<View, String> forecastIconPair = Pair.create(ivIcon, getActivity().getString(R.string.forecast_icon_transition_name));
            Pair<View, String> forecastTemp = Pair.create(tvTemperature, getString(R.string.forecast_temperature_transition_name));
            Pair<View, String> forecastUnit = Pair.create(tvUnit, getString(R.string.forecast_unit_transition_name));
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    forecastIconPair,
                    forecastTemp,
                    forecastUnit
            );

            startActivity(intent, optionsCompat.toBundle());
        }
    }
}
