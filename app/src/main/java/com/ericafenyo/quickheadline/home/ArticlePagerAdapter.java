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

import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ericafenyo.quickheadline.model.News;

import java.util.List;

/**
 * Created by eric on 03/03/2018.
 * view pager adapter
 */

public class ArticlePagerAdapter extends FragmentStatePagerAdapter {

    private List<News.Article> mData;
    private int mPosition;


    public ArticlePagerAdapter(Fragment fragment, List<News.Article> mData, int position) {
        super(fragment.getChildFragmentManager());
        this.mData = mData;
        this.mPosition = position;
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleDetailFragment.newInstance(mData.get(position), this.mPosition);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
