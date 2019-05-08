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

package com.example.eric.quickheadline.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eric on 16/02/2018.
 */

public class ArticleCategory implements Parcelable{
    private Drawable drawable;
    private String category;

    public ArticleCategory(Drawable drawable, String category) {
        this.drawable = drawable;
        this.category = category;
    }

    protected ArticleCategory(Parcel in) {
        category = in.readString();
    }

    public static final Creator<ArticleCategory> CREATOR = new Creator<ArticleCategory>() {
        @Override
        public ArticleCategory createFromParcel(Parcel in) {
            return new ArticleCategory(in);
        }

        @Override
        public ArticleCategory[] newArray(int size) {
            return new ArticleCategory[size];
        }
    };

    public Drawable getDrawable() {
        return drawable;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
    }
}
