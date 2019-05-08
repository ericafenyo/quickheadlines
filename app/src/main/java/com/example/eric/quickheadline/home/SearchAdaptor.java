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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.model.News;
import com.example.eric.quickheadline.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 07/03/2018.
 * a RecyclerView adapter class
 */

public class SearchAdaptor extends RecyclerView.Adapter<SearchAdaptor.SearchViewHolder> {

    private Context mContext;
    private List<News.Article> mData;
    private onItemSelected mOnItemSelected;


    public SearchAdaptor(Context mContext, onItemSelected mOnItemSelected) {
        this.mContext = mContext;
        this.mOnItemSelected = mOnItemSelected;
    }

    public void setData(List<News.Article> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public SearchAdaptor.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .list_item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        News.Article articles = mData.get(position);

        holder.tvArticleTitle.setText(articles.getTitle());
        holder.tvArticleSource.setText(articles.getSource().getName());
        String utcDate = DateUtils.getUTCDate(articles.getPublishedAt());
        holder.tvArticlePublishedDate.setText(DateUtils.getLocalDate(utcDate));

        GlideApp.with(mContext).load(articles.getUrlToImage()).placeholder((R.color.colorGrayLight))
                .into(holder.ivArticleThumbnail);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
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

    //Item click listener Interface
    public interface onItemSelected {
        void onClick(String url);

        void onLongClick(Bookmark bookmark);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.tv_search_article_title)
        TextView tvArticleTitle;
        @BindView(R.id.tv_search_article_source)
        TextView tvArticleSource;
        @BindView(R.id.tv_search_article_publish_time)
        TextView tvArticlePublishedDate;
        @BindView(R.id.iv_search_article_thumbnail)
        ImageView ivArticleThumbnail;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemSelected.onClick(mData.get(getAdapterPosition()).getUrl());
        }

        @Override
        public boolean onLongClick(View v) {
            mOnItemSelected.onLongClick(provideBookmark(getAdapterPosition()));
            return true;
        }
    }
}
