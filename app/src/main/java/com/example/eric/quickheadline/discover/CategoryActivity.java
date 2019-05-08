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

package com.example.eric.quickheadline.discover;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.home.WebActivity;
import com.example.eric.quickheadline.bookmark.BookmarkDb;
import com.example.eric.quickheadline.bookmark.BookmarkRepositoryImpl;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.home.SearchAdaptor;
import com.example.eric.quickheadline.model.ArticleCategory;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.utils.CategoryUtils;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * displays article category
 */
public class CategoryActivity extends AppCompatActivity implements SearchAdaptor.onItemSelected {
    private static final String LOG_TAG = CategoryActivity.class.getName();//for debugging purpose
    private String mCategory;
    private SearchAdaptor mAdapter;
    @BindView(R.id.recycler_view_category)
    RecyclerView recyclerView;
    @BindView(R.id.coordinator_layout_category)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_bar_category)
    ProgressBar progressBar;
    @BindView(R.id.iv_detail_category)
    ImageView ivCategory;


    @Inject
    SharedPreferences preferences;
    private PreferenceUtils preferenceUtils;

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(ConstantFields.EXTRA_ARTICLE_ITEM_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catergoy);
        ButterKnife.bind(this);
        ((MyApp) getApplication()).getComponent().inject(this);
        preferenceUtils = new PreferenceUtils(preferences);
        supportPostponeEnterTransition();
        if (!HelperUtils.hasInternetConnectivity(this)){
            HelperUtils.toast(getApplicationContext(),getString(R.string.error_no_internet));
            progressBar.setVisibility(View.INVISIBLE);
        }

        List<ArticleCategory> categories = CategoryUtils.getArticleCategories(getApplication());

        Toolbar toolbar = findViewById(R.id.toolbar_category);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(ConstantFields.EXTRA_ARTICLE_ITEM_POSITION);
            mCategory = categories.get(position).getCategory();
            //noinspection ConstantConditions
            getSupportActionBar().setTitle(mCategory);
            Drawable drawable = categories.get(position).getDrawable();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivCategory.setTransitionName(mCategory);
            }

            GlideApp.with(this).load(drawable).placeholder(R.color.colorPrimary).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    supportStartPostponedEnterTransition();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    supportStartPostponedEnterTransition();
                    return false;
                }
            }).into(ivCategory);
        }
        mAdapter = new SearchAdaptor(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DiscoverViewModelFactory factory = new DiscoverViewModelFactory(getApplication(), mCategory);
        DiscoverViewModel viewModel = ViewModelProviders.of(this, factory).get(DiscoverViewModel.class);
        viewModel.getArticleByCategory().observe(this, articles -> {
            HelperUtils.hideLoading(recyclerView, progressBar);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setData(articles);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
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
        Intent intent = WebActivity.newIntent(this, url);
        startActivity(intent);
    }

    @Override
    public void onLongClick(Bookmark bookmark) {
        try {
            long[] inserts = BookmarkRepositoryImpl.performInsert(BookmarkDb.getINSTANCE(getApplication()), bookmark);
            if (inserts.length > 0) {
                HelperUtils.makeSnack(coordinatorLayout, "Bookmark Added");
                preferenceUtils.setIsBookmarked(bookmark.getUrl(), true);
            }
        } catch (SQLiteConstraintException e) {
            Log.e(LOG_TAG, "Constraint Exception: " + e);

        }
    }


}
