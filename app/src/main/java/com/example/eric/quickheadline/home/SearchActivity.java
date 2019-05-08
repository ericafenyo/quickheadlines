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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.bookmark.BookmarkDb;
import com.example.eric.quickheadline.bookmark.BookmarkRepositoryImpl;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.quickheadline.utils.ConstantFields.EXTRA_ARTICLE_LIST;

/**
 * the class displays search results
 */
public class SearchActivity extends AppCompatActivity implements SearchAdaptor.onItemSelected {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private SearchAdaptor mAdaptor;

    @BindView(R.id.coordinator_layout_search)
    CoordinatorLayout coordinatorLayout;
    @Inject
    SharedPreferences preferences;
    private PreferenceUtils preferenceUtils;


    public static Intent newIntent(Context context, List<News.Article> articles) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ARTICLE_LIST, (ArrayList<? extends Parcelable>) articles);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ((MyApp) getApplication()).getComponent().inject(this);
        preferenceUtils = new PreferenceUtils(preferences);

        mToolbar = findViewById(R.id.toolbar_search);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setTitle("Search");

            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = findViewById(R.id.recycler_view_search);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<News.Article> data = bundle.getParcelableArrayList(EXTRA_ARTICLE_LIST);
            mAdaptor = new SearchAdaptor(this, this);
            mAdaptor.setData(data);
            mRecyclerView.setAdapter(mAdaptor);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setHasFixedSize(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String url) {
        HelperUtils.viewOnWeb(getApplication(), url);
    }

    @Override
    public void onLongClick(Bookmark bookmark) {

        BookmarkDb db = BookmarkDb.getINSTANCE(getApplication());
        long[] longs = BookmarkRepositoryImpl.performInsert(db, bookmark);
        if (longs.length > 0) {
            HelperUtils.makeSnack(coordinatorLayout, "Bookmark added");
            preferenceUtils.setIsBookmarked(bookmark.getUrl(), true);

        }
    }
}
