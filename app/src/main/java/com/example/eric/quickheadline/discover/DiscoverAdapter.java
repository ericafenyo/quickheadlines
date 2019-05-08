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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.model.ArticleCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 17/02/2018.
 * a simple RecyclerView adapter class
 */

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {
    private Context mContext;
    private List<ArticleCategory> mData;
    private onItemSelected mOnItemSelected;

    public DiscoverAdapter(Context mContext) {
        this.mContext = mContext;
        this.mOnItemSelected = new ItemSelectedImpl(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //Item click listener Interface
    public interface onItemSelected {
        void onClick(int position, ImageView view);
    }

    public void setData(List<ArticleCategory> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscoverViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        holder.tvDiscoverCategory.setText(mData.get(position).getCategory());
        holder.ivDiscoverCategory.setImageDrawable(mData.get(position).getDrawable());
        ViewCompat.setTransitionName(holder.ivDiscoverCategory, mData.get(position).getCategory());
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_discover_category)
        ImageView ivDiscoverCategory;
        @BindView(R.id.tv_discover_category)
        TextView tvDiscoverCategory;

        public DiscoverViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemSelected.onClick(getAdapterPosition(), ivDiscoverCategory);
        }
    }

    private class ItemSelectedImpl implements onItemSelected {
        private Context mContext;

        public ItemSelectedImpl(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onClick(int position, ImageView view) {
            Intent intent = CategoryActivity.newIntent(mContext, position);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, view, mData.get(position).getCategory());
            mContext.startActivity(intent,optionsCompat.toBundle());
        }
    }
}


