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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.SettingsActivity;
import com.example.eric.quickheadline.model.Bookmark;
import com.example.eric.quickheadline.utils.HelperUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * this fragment displays article bookmarks
 */
public class BookmarkFragment extends Fragment implements BookmarkAdapter.onItemSwipe {

    private BookmarkAdapter mAdaptor;

    @BindView(R.id.recycler_view_bookmark)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_bookmark)
    Toolbar toolbar;
    @BindView(R.id.linear_layout_empty_state_message)
    LinearLayout layoutEmptyState;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(),R.drawable.ic_round_menu_24px));
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_bookmark);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ButterKnife.bind(this, view);
        mAdaptor = new BookmarkAdapter(getActivity().getApplication(), this);

        return view;
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

    public void onStart() {
        super.onStart();
        if (isAdded()) {
            BookmarkViewModel viewModel = ViewModelProviders.of(getActivity()).get(BookmarkViewModel.class);
            if (viewModel.getBookmark() != null) {
                viewModel.getBookmark().observe(getActivity(), bookmarks -> {

                    if (bookmarks.isEmpty()) {
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    }
                    mAdaptor.setData(bookmarks);
                    recyclerView.setAdapter(mAdaptor);
                    recyclerView.setHasFixedSize(true);

                    if (isLandscapeMode()) {
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    } else if (isPortraitMode()) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    recyclerView.setNestedScrollingEnabled(false);
                });
            }
        }
    }

    @Override
    public void onSwiped(Bookmark bookmark, RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof BookmarkAdapter.BookmarkViewHolder) {
            mAdaptor.removeItem(viewHolder.getAdapterPosition());
        }
    }


    private boolean isLandscapeMode() {
        return getActivity() != null && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private boolean isPortraitMode() {
        return getActivity() != null && getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}