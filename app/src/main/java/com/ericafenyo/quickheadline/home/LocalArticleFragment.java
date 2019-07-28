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
package com.ericafenyo.quickheadline.home;


import static android.provider.BaseColumns._ID;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ericafenyo.quickheadline.R;
import com.ericafenyo.quickheadline.SettingsActivity;
import com.ericafenyo.quickheadline.contract.ArticleEntry;
import com.ericafenyo.quickheadline.di.MyApp;
import com.ericafenyo.quickheadline.home.ArticleAdapter.onItemSelected;
import com.ericafenyo.quickheadline.model.News;
import com.ericafenyo.quickheadline.model.News.Article;
import com.ericafenyo.quickheadline.utils.HelperUtils;
import com.ericafenyo.quickheadline.utils.PreferenceUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalArticleFragment extends Fragment implements onItemSelected {

    private static final int ID_ARTICLE_LOADER = 250;

    private ArticleAdapter mAdapter;

    @BindView(R.id.toolbar_home) Toolbar toolbar;
    @BindView(R.id.recycler_view_home) RecyclerView recyclerView;
    @BindView(R.id.progress_bar_home) ProgressBar progressBar;

    @Inject
    PreferenceUtils preferenceUtils;

    public static LocalArticleFragment newInstance() {
        return new LocalArticleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        /* Inflate the layout for this fragment and setup ButterKnife*/
        View view = inflater.inflate(R.layout.local_article_fragment, container, false);
        ButterKnife.bind(this, view);
        ((MyApp) getActivity().getApplication()).getComponent().inject(this);

        mAdapter = new ArticleAdapter(requireContext(), this);

        LoaderManager.getInstance(this).initLoader(ID_ARTICLE_LOADER, null, loaderArticle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(R.string.title_top_stories);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
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
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // ArticleAdapter#onItemSelected Implementations

    @Override
    public void onClick(int position, List<Article> articles) {
        String fragTAG = ArticlePagerFragment.class.getName();
        Fragment fragment = ArticlePagerFragment.newInstance(articles, position);
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.frame_container_main, fragment, fragment.getTag())
            .addToBackStack(fragTAG)
            .commit();
    }

    // Article CursorLoader Callbacks

    /**
     * Cursor loader for querying News Article from out database
     */
    private LoaderManager.LoaderCallbacks<Cursor> loaderArticle = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

            switch (loaderId) {
                case ID_ARTICLE_LOADER:

                    //noinspection ConstantConditions
                    return new CursorLoader(getActivity(), ArticleEntry.CONTENT_URI_ARTICLE,
                        null,
                        null, null,
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

            List<Article> articles = new ArrayList<>();
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

            }

            populateRecyclerView(articles);

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }
    };

    private void populateRecyclerView(List<Article> data) {
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
}