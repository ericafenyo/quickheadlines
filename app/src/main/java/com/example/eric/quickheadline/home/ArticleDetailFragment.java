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
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.bookmark.BookmarkDb;
import com.example.eric.quickheadline.bookmark.BookmarkRepositoryImpl;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.model.News.Article;
import com.example.eric.quickheadline.utils.ConstantFields;
import com.example.eric.quickheadline.utils.DateUtils;
import com.example.eric.quickheadline.utils.HelperUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass for displaying news article details.
 */
public class ArticleDetailFragment extends Fragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private static final String LOG_TAG = ArticleDetailFragment.class.getName();//for debugging purpose

    private String source;
    private String title;
    private String description;
    private String publishedAt;
    private String urlToImage;
    private String url;
    private String author;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_detail_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.btn_detail_read_more)
    Button btnDetailReadMore;
    @BindView(R.id.tv_detail_article_description)
    TextView tvDetailArticleDescription;
    @BindView(R.id.tv_detail_article_source)
    TextView tvDetailArticleSource;
    @BindView(R.id.tv_detail_article_published_time)
    TextView tvDetailArticlePublishedTime;
    @BindView(R.id.iv_detail_article_thumbnail)
    ImageView ivDetailArticleThumbnail;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Inject
    PreferenceUtils preferenceUtils;

    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    public static ArticleDetailFragment newInstance(Article article, int position) {
        Bundle args = new Bundle();
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        args.putParcelable(ConstantFields.ARGUMENT_ARTICLE_ITEM, article);
        args.putInt(ConstantFields.ARGUMENT_ARTICLE_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //configure toolbar
        toolbar.inflateMenu(R.menu.menu_detail);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, view);
        ((MyApp) getActivity().getApplication()).getComponent().inject(this);

        //retrieve data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            Article article = bundle.getParcelable(ConstantFields.ARGUMENT_ARTICLE_ITEM);
            title = article.getTitle();
            description = article.getDescription();
            publishedAt = article.getPublishedAt();
            urlToImage = article.getUrlToImage();
            url = article.getUrl();
            author = article.getAuthor();
            source = article.getSource().getName();

            //set article image thumbnail
            GlideApp.with(this).load(urlToImage).placeholder(R.color.colorGrayLight).into(ivDetailArticleThumbnail);
            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fira_sans_medium.ttf");


            //set article title

                tvArticleTitle.setText(title);
                tvArticleTitle.setTypeface(custom_font);


            //set article description

                tvDetailArticleDescription.setText(description);
                tvDetailArticleDescription.setTextSize(Float.parseFloat(preferenceUtils.getArticleTextSize()));

            //set article published time
            tvDetailArticlePublishedTime.setText(DateUtils.getLocalDate(DateUtils.getUTCDate(publishedAt)));
            //set article source
            tvDetailArticleSource.setText(source);
        }

        fab.setOnClickListener(this);
        btnDetailReadMore.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        validateBookmarkEntry();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                setBookmark(url);
                break;
            case R.id.btn_detail_read_more:
                loadMoreDetail();
                break;
        }
    }

    private void loadMoreDetail() {
        Intent webViewIntent = WebActivity.newIntent(getActivity(), url);
        startActivity(webViewIntent);
    }

    private void addToBookmark() {
        Bookmark bookmark = provideBookmark();

        try {
            long[] inserts = BookmarkRepositoryImpl.performInsert(BookmarkDb.getINSTANCE(getActivity()), bookmark);
            if (inserts.length > 0) {
                setMarkedIcon();
                preferenceUtils.setIsBookmarked(bookmark.getUrl(), true);
                HelperUtils.makeSnack(getView(), getActivity().getString(R.string.msg_bookmark_added));
            }
        } catch (SQLiteConstraintException e) {
            Log.e(LOG_TAG, "Constraint Exception: " + e);

        }
    }

    private Bookmark provideBookmark() {
        return new Bookmark.Builder()
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setPublishedAt(publishedAt)
                .setSource(source)
                .setUrl(url)
                .setUrlToImage(urlToImage)
                .build();
    }

    private void removeBookmark() {
        Bookmark bookmark = provideBookmark();
        if (bookmark != null) {
            int delete = BookmarkRepositoryImpl.performDelete(BookmarkDb.getINSTANCE(getActivity()), bookmark);
            if (delete > 0) {
                setUnmarkedIcon();
                preferenceUtils.setIsBookmarked(bookmark.getUrl(), false);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String shareBody = title.toUpperCase() + "\n \n" + description + "\n \n" + url +
                        "\n\n " + "Powered by NewsAPI.org";
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String subject = "News Article";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.title_share_using)));
        }
        return false;
    }


    public void setBookmark(String id) {
        boolean isBookmarked = preferenceUtils.isBookmarked(id);
        if (!isBookmarked) {
            addToBookmark();
        } else {
            removeBookmark();
        }
    }

    /**
     * change the floating action button icon when clicked
     */
    public void setMarkedIcon() {
        Drawable myBookmarkSrc = ContextCompat.getDrawable(getActivity(), R.drawable.ic_bookmark_ticked);
        fab.setImageDrawable(myBookmarkSrc);

    }

    /**
     * change the floating action button icon when clicked
     */
    public void setUnmarkedIcon() {
        Drawable myBookmarkSrc = ContextCompat.getDrawable(getActivity(), R.drawable.ic_bookmark);
        fab.setImageDrawable(myBookmarkSrc);
    }

    /**
     * change the floating action button icon based on sharedPreference boolean values
     * stored with articles url as a key
     */
    public void validateBookmarkEntry() {
        boolean isBookmarked = preferenceUtils.isBookmarked(url);
        if (!isBookmarked) {
            setUnmarkedIcon();
        } else {
            setMarkedIcon();
        }
    }
}