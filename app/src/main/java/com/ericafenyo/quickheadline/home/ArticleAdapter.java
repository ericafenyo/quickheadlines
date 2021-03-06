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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ericafenyo.quickheadline.R;
import com.ericafenyo.quickheadline.di.GlideApp;
import com.ericafenyo.quickheadline.model.Bookmark;
import com.ericafenyo.quickheadline.model.News;
import com.ericafenyo.quickheadline.utils.Moment;
import java.util.List;

/**
 * Created by eric on 07/03/2018. a simple RecyclerView adapter class
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context mContext;
    private List<News.Article> mData;
    private onItemSelected mOnItemSelected;
    private static final String LOG_TAG = ArticleAdapter.class.getName();

    public ArticleAdapter(Context mContext, onItemSelected mOnItemSelected) {
        this.mContext = mContext;
        this.mOnItemSelected = mOnItemSelected;
    }

    public void setData(List<News.Article> mData) {
        this.mData = mData;
    }

    //Item click listener Interface
    public interface onItemSelected {

        void onClick(int position, List<News.Article> articles);

        void onLongClick(Bookmark bookmark);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
            .list_item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        News.Article articles = mData.get(position);
        holder.textArticleTitle.setText(articles.getTitle());

        String sourceName = articles.getSource().getName();
        String source;
        if (sourceName.indexOf('.') != -1) {
            source = sourceName.substring(0, sourceName.indexOf('.'));
        } else {
            source = sourceName;
        }

        holder.textArticleSource.setText(source);
        String timeAgo = Moment.getTimeAgo(articles.getPublishedAt());
        holder.textArticlePublishedTime.setText(timeAgo);

        GlideApp.with(mContext).load(articles.getUrlToImage()).placeholder((R.color.colorGrayLight))
            .into(holder.imageArticleThumbnail);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

        @BindView(R.id.image_article_thumbnail) ImageView imageArticleThumbnail;
        @BindView(R.id.text_article_source) TextView textArticleSource;
        @BindView(R.id.text_article_title) TextView textArticleTitle;
        @BindView(R.id.text_article_published_time) TextView textArticlePublishedTime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemSelected.onClick(getAdapterPosition(), mData);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnItemSelected.onLongClick(provideBookmark(getAdapterPosition()));
            return true;
        }
    }

    /**
     * @param position The adapter position of the item if it still exists in the adapter.
     * @return an object of {@link Bookmark}
     */
    private Bookmark provideBookmark(int position) {
        News.Article articles = mData.get(position);
        return new Bookmark.Builder().setTitle(articles.getTitle())
            .setDescription(articles.getDescription())
            .setSource(articles.getSource().getName())
            .setPublishedAt(articles.getPublishedAt())
            .setAuthor(articles.getAuthor())
            .setUrl(articles.getUrl())
            .setUrlToImage(articles.getUrlToImage())
            .build();
    }
}
