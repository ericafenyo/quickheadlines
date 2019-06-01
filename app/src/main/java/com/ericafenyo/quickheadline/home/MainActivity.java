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

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ericafenyo.quickheadline.ClosingService;
import com.ericafenyo.quickheadline.R;
import com.ericafenyo.quickheadline.bookmark.BookmarkFragment;
import com.ericafenyo.quickheadline.di.MyApp;
import com.ericafenyo.quickheadline.discover.DiscoverFragment;
import com.ericafenyo.quickheadline.sync.ArticleSyncUtils;
import com.ericafenyo.quickheadline.sync.WeatherSyncUtils;
import com.ericafenyo.quickheadline.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import javax.inject.Inject;

/**
 * this class handles the bottom navigation and it's associated fragments
 */
public class MainActivity extends AppCompatActivity implements
    BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getName();//for debugging purpose

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Inject
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyApp) getApplication()).getComponent().inject(this);

        //start data sync...
        ArticleSyncUtils.initialize(getApplication());
        WeatherSyncUtils.initialize(getApplication());
        startService(new Intent(MainActivity.this, ClosingService.class));

        navigation.setOnNavigationItemSelectedListener(this);
        //select first navigation menu on startup
        if (preferenceUtils.isFirstTimeChecked()) {
            View view = navigation.findViewById(R.id.action_headlines);
            view.performClick();
            preferenceUtils.setFirstTimeChecked(false);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame_container_main);
            if (fragment != null) {
                updateNavMenuItem(fragment);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if the app is closed and it is not undergoing configuration Changes
        //then we set the FirstTimeChecked value to true so that the first navigationView menu item is reselected when the user
        //reopen the app again
        if (!this.isChangingConfigurations() && this.isFinishing()) {
            preferenceUtils.setFirstTimeChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_headlines:
                fragment = HomeFragment.newInstance();
                loadFragment(fragment);
                return true;
            case R.id.action_explore:
                fragment = DiscoverFragment.newInstance();
                loadFragment(fragment);

                return true;
            case R.id.action_favorites:
                fragment = BookmarkFragment.newInstance();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        String TAG = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(TAG, 0);

        // load new fragment only if it is not in back stack
        if (!fragmentPopped && manager.findFragmentByTag(TAG) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_container_main, fragment, TAG);
            transaction.addToBackStack(TAG);
            transaction.commit();
        }
    }

    /**
     * Sets bottomNavigationMenuItem as checked after the fragment is pop back from backStack
     *
     * @param fragment {@link Fragment}
     */
    private void updateNavMenuItem(Fragment fragment) {
        String fragClassName = fragment.getClass().getName();
        final int homeMenuItemIndex = 0;
        final int discoverMenuItemIndex = 1;
        final int bookmarkMenuItemIndex = 2;

        if (fragClassName.equals(HomeFragment.class.getName())) {
            navigation.getMenu().getItem(homeMenuItemIndex).setChecked(true);

        } else if (fragClassName.equals(DiscoverFragment.class.getName())) {
            navigation.getMenu().getItem(discoverMenuItemIndex).setChecked(true);

        } else if (fragClassName.equals(BookmarkFragment.class.getName())) {
            navigation.getMenu().getItem(bookmarkMenuItemIndex).setChecked(true);
        }
    }
}