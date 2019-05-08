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

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eric.quickheadline.model.OSLicense;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 11/03/2018.
 * a simple listAdapter class
 */

public class LicenseAdapter extends BaseAdapter {

    private Context mContext;
    private List<OSLicense> mData;

    public LicenseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OSLicense> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LicenseViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_license, parent, false);
            holder = new LicenseViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (LicenseViewHolder) view.getTag();
        }
        onBindViewHolder(holder, position);
        return view;
    }

    private void onBindViewHolder(@NonNull LicenseViewHolder holder, int position) {
        OSLicense osLicense = (OSLicense) getItem(position);
        holder.tvLibName.setText(osLicense.getName());
        holder.tvLibDescription.setText(osLicense.getDescription());
    }

    public class LicenseViewHolder {
        private View mItemView;
        @BindView(R.id.tv_lib_name)
        TextView tvLibName;
        @BindView(R.id.tv_lib_description)
        TextView tvLibDescription;

        public LicenseViewHolder(View itemView) {
            this.mItemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
