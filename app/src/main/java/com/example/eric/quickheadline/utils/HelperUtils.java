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

package com.example.eric.quickheadline.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eric.quickheadline.BuildConfig;
import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.di.ApiEndpoint;
import com.example.eric.quickheadline.di.MyApp;
import com.example.eric.quickheadline.home.SearchActivity;
import com.example.eric.quickheadline.home.WebActivity;
import com.example.eric.quickheadline.model.News;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.eric.quickheadline.utils.ConstantFields.QUERY_PARAMS_PAGE_SIZE;

/**
 * Created by eric on 04/02/2018.
 * simple class with some helper methods
 */

public class HelperUtils {
    private static final String TAG = HelperUtils.class.getName();
    private static Toast toast;


    //make toast message
    public static void toast(Context context, Object message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, String.valueOf(message), Toast.LENGTH_SHORT);
        toast.show();
    }

    //Snackbar message with action
    public static void makeSnackAction(View view, String message, String actionMessage, View.OnClickListener onClickListener) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction(actionMessage, onClickListener)
                .setDuration(3000)
                .show();
    }

    //Snackbar message
    public static void makeSnack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * @param context {@link android.app.Application}
     * @param param   the  query params tpo search for
     */
    public static void query(Context context, String param) {

        ApiEndpoint endpoint = ((MyApp) context).getComponent().getArticleEndpoint();
        if (HelperUtils.hasInternetConnectivity(context)) {
            endpoint.getSearchRepose(param, QUERY_PARAMS_PAGE_SIZE, BuildConfig.NEWS_API_KEY).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                    if (response.body() != null) {

                        Intent intent = SearchActivity.newIntent(context, response.body().getArticles());
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                    Log.e(TAG, context.getString(R.string.error_search_failed) + t);
                }
            });
        } else {
            HelperUtils.toast(context, context.getString(R.string.error_no_internet));
        }
    }

    public static void viewOnWeb(Context context, String webUrl) {
        Intent webViewIntent = WebActivity.newIntent(context, webUrl);
        webViewIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(webViewIntent);
    }

    /**
     * a simple http connection that returns a string response
     */
    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

    public static boolean isLandscapeMode(Context context) {
        return context != null && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortraitMode(Context context) {
        return context != null && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * shows the data to the user and hides the progress bar
     */
    public static void showLoading(RecyclerView recyclerView, ProgressBar progressBar) {
        if (recyclerView != null && progressBar != null) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * hides the recyclerView and show the loading  status
     */
    public static void hideLoading(RecyclerView recyclerView, ProgressBar progressBar) {
        if (recyclerView != null && progressBar != null) {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * returns a boolean based on the internet connectivity of the device
     *
     * @param context Context
     * @return true if device is connected to internet
     */
    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
