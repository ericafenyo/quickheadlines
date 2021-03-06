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

package com.ericafenyo.quickheadline.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ericafenyo.quickheadline.R;
import com.ericafenyo.quickheadline.SettingsActivity;
import com.ericafenyo.quickheadline.model.ArticleCategory;
import com.ericafenyo.quickheadline.utils.CategoryUtils;
import com.ericafenyo.quickheadline.utils.HelperUtils;
import java.util.List;


/**
 * a simple fragment class to display article categories
 */
public class DiscoverFragment extends Fragment {

    @BindView(R.id.recycler_view_discover)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_discover)
    Toolbar toolbar;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, view);
        List<ArticleCategory> mCategories = CategoryUtils.getArticleCategories(getActivity());
        DiscoverAdapter mAdapter = new DiscoverAdapter(getActivity());
        mAdapter.setData(mCategories);
        recyclerView.setAdapter(mAdapter);

        if (HelperUtils.isLandscapeMode(getActivity())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else if (HelperUtils.isPortraitMode(getActivity())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(
                ContextCompat.getDrawable(getActivity(), R.drawable.ic_round_menu_24px));
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(R.string.title_discover);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        //perform initialization
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //set search hint message and listener
        searchView.setQueryHint(getResources().getString(R.string.search_hint_text));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //noinspection ConstantConditions
                HelperUtils.query(getActivity().getApplication(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
