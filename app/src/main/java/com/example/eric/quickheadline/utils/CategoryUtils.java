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
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.eric.quickheadline.R;
import com.example.eric.quickheadline.model.ArticleCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 17/02/2018.
 */

public class CategoryUtils {
    /**
     * returns list of {@link ArticleCategory}
     */
    public static List<ArticleCategory> getArticleCategories(Context context) {

        List<ArticleCategory> categories = new ArrayList<>();

        String[] stringArray = context.getResources().getStringArray(R.array.article_category);
        int[] drawableIDs = getDrawableId();

        for (int i = 0; i < stringArray.length; i++) {
            String category = stringArray[i];
            int drawableID = drawableIDs[i];

            categories.add(new ArticleCategory(getCategoryDrawable(context, drawableID), category));
        }
        return categories;
    }

    public static Drawable getCategoryDrawable(Context context, int drawableID) {
        return ContextCompat.getDrawable(context, drawableID);
    }

    public static int[] getDrawableId() {

        return new int[]{
                R.drawable.business,
                R.drawable.entertainment,
                R.drawable.general,
                R.drawable.health,
                R.drawable.science,
                R.drawable.sports,
                R.drawable.technology,
        };
    }
}
