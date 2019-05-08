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

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.transition.Explode;
import android.view.Window;

import com.example.eric.quickheadline.utils.JsonUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import static com.example.eric.quickheadline.utils.ConstantFields.FILE_IO_NAME;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latValue;
    private double lngValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            String s = JsonUtils.readFile(this, FILE_IO_NAME);
            JSONArray array = new JSONArray(s);
            final int latIndex = 0;
            final int lngIndex = 1;
            String lat = (String) array.get(latIndex);
            String lng = (String) array.get(lngIndex);
            latValue = Double.parseDouble(lat);
            lngValue = Double.parseDouble(lng);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        // Add a marker in location and move the camera
        LatLng latLng = new LatLng(latValue, lngValue);
        mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }
}
