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

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericafenyo.quickheadline.R;
import com.ericafenyo.quickheadline.model.News.Article;
import com.ericafenyo.quickheadline.utils.ConstantFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 25/02/2018.
 * a viewpager fragment
 * this fragment is used to populate {@link ArticleDetailFragment}
 */

public class ArticlePagerFragment extends Fragment {

    public static ArticlePagerFragment newInstance(List<Article> articles, int position) {
        Bundle args = new Bundle();
        ArticlePagerFragment fragment = new ArticlePagerFragment();
        args.putParcelableArrayList(ConstantFields.ARGUMENT_ARTICLE_LIST, (ArrayList<? extends Parcelable>) articles);
        args.putInt(ConstantFields.ARGUMENT_ARTICLE_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = (ViewPager) inflater.inflate(R.layout.fragment_view_pager, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            List<Article> articles = bundle.getParcelableArrayList(ConstantFields.ARGUMENT_ARTICLE_LIST);
            /*Configure Viewpager Adapter*/
            int position = bundle.getInt(ConstantFields.ARGUMENT_ARTICLE_POSITION);
            ArticlePagerAdapter adaptor = new ArticlePagerAdapter(this, articles, position);
            viewPager.setAdapter(adaptor);
            viewPager.setCurrentItem(position);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
        return viewPager;
    }
}
