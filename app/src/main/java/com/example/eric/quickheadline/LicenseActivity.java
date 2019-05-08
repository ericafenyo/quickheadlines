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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.eric.quickheadline.model.OSLicense;
import com.example.eric.quickheadline.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * an Activity that displays detailed license for open source libraries used in the app
 */
public class LicenseActivity extends AppCompatActivity {

    private static final String TAG = LicenseActivity.class.getName();
    private LicenseAdapter mAdapter;
    @BindView(R.id.list_view_license)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar_license);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            getSupportActionBar().setTitle("License details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //initialize the Adapter
        mAdapter = new LicenseAdapter(this);

        String jsonFileName = "json/open_source_library_license.json";
        Type type = new TypeToken<List<OSLicense>>() {
        }.getType();

        try {
            String jsonString = JsonUtils.loadJSONFromAsset(this, jsonFileName);
            List<OSLicense> osLicenses = JsonUtils.deSerializeList(jsonString, type);
            if (osLicenses != null) {
                mAdapter.setData(osLicenses);
                listView.setAdapter(mAdapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading json file: " + e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

