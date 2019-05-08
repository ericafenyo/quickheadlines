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


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.eric.quickheadline.utils.ConstantFields;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;


    public WebFragment() {
        // Required empty public constructor
    }


    public static WebFragment newInstance() {
        return new WebFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_web_view);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        mWebView = view.findViewById(R.id.web_view);
        mProgressBar = view.findViewById(R.id.progress_bar_web_view);
        toolbar = view.findViewById(R.id.toolbar_web);

        Bundle bundle = getActivity().getIntent().getExtras();

        if (bundle != null) {
            String url = bundle.getString(ConstantFields.EXTRA_WEB_VIEW_URL);
            mWebView.loadUrl(url);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(false);
            mWebView.setWebViewClient(new mViewClient());
        }
        return view;
    }

    private void hideLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private class mViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String requestUrl) {
            super.onPageFinished(view, requestUrl);
            hideLoading();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String requestUrl) {
            mWebView.loadUrl(requestUrl);
            return super.shouldOverrideUrlLoading(view, requestUrl);
        }
    }
}
