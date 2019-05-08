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

package com.example.eric.quickheadline.bookmark;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.home.WebActivity;
import com.example.eric.quickheadline.di.GlideApp;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.utils.DateUtils;
import com.example.eric.quickheadline.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 07/03/2018.
 * a simple RecyclerView adapter class
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private Context mContext;
    private List<Bookmark> mData;
    private onItemSelected mOnItemSelected;
    private onItemSwipe mOnItemSwipe;
    private PreferenceUtils preferenceUtils;

    public BookmarkAdapter(Context mContext, onItemSwipe mOnItemSwipe) {
        this.mContext = mContext;
        this.mOnItemSwipe = mOnItemSwipe;
        this.mOnItemSelected = new ItemSelectedImpl(mContext);
        preferenceUtils = ((MyApp) mContext).getComponent().getPreferenceUtils();
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookmarkViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .list_item_bookmark, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        Bookmark bookmark = mData.get(position);

        holder.tvArticleTitle.setText(bookmark.getTitle());
        holder.tvArticleDescription.setText(bookmark.getDescription());
        String utcDate = DateUtils.getUTCDate(bookmark.getPublishedAt());
        holder.tvArticlePublishedDate.setText(DateUtils.getLocalDate(utcDate));

        GlideApp.with(mContext).load(bookmark.getUrlToImage()).placeholder((R.color.colorGrayLight))
                .into(holder.ivArticleThumbnail);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        mOnItemSwipe.onSwiped(provideBookmark(viewHolder.getAdapterPosition()), viewHolder, direction);
                    }
                };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }


    public void removeItem(int position) {
        Bookmark bookmark = provideBookmark(position);
        int delete = BookmarkRepositoryImpl.performDelete(BookmarkDb.getINSTANCE(mContext), bookmark);
        if (delete > 0) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(position);
            preferenceUtils.setIsBookmarked(bookmark.getUrl(), false);
        }
    }


    public class BookmarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_bookmark_article_thumbnail)
        ImageView ivArticleThumbnail;
        @BindView(R.id.tv_bookmark_article_title)
        TextView tvArticleTitle;
        @BindView(R.id.tv_bookmark_article_description)
        TextView tvArticleDescription;
        @BindView(R.id.tv_bookmark_article_publish_date)
        TextView tvArticlePublishedDate;

        public BookmarkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnItemSelected.onClick(mData.get(getAdapterPosition()).getUrl());
        }
    }

    public void setData(List<Bookmark> mData) {
        this.mData = mData;
    }

    private Bookmark provideBookmark(int position) {
        return mData.get(position);
    }

    //Item click listener Interface
    private interface onItemSelected {
        void onClick(String url);
    }

    //Item swipe listener Interface
    public interface onItemSwipe {
        void onSwiped(Bookmark bookmark, RecyclerView.ViewHolder viewHolder, int direction);
    }

    private class ItemSelectedImpl implements onItemSelected {
        private Context mContext;

        public ItemSelectedImpl(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onClick(String url) {
            Intent intent = WebActivity.newIntent(mContext, url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
    }
}
