
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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * simple POJO class for News Endpoint
 * 1.{@link News}
 * 2.{@link Article}
 * 3.{@link Source}
 */
public class News {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("articles")
    @Expose
    private List<Article> articles = null;

    ////////// News Getters ////////////////////

    public String getStatus() {
        return status;
    }

    public List<Article> getArticles() {
        return articles;
    }


    public static class Article implements Parcelable {

        @SerializedName("source")
        @Expose
        private Source source;
        @SerializedName("author")
        @Expose
        private String author;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("urlToImage")
        @Expose
        private String urlToImage;
        @SerializedName("publishedAt")
        @Expose
        private String publishedAt;

        //Constructor
        public Article(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt) {
            this.source = source;
            this.author = author;
            this.title = title;
            this.description = description;
            this.url = url;
            this.urlToImage = urlToImage;
            this.publishedAt = publishedAt;
        }


        ////////// Article Parcelable ////////////////////

        protected Article(Parcel in) {
            source = in.readParcelable(Source.class.getClassLoader());
            author = in.readString();
            title = in.readString();
            description = in.readString();
            url = in.readString();
            urlToImage = in.readString();
            publishedAt = in.readString();
        }

        public static final Creator<Article> CREATOR = new Creator<Article>() {
            @Override
            public Article createFromParcel(Parcel in) {
                return new Article(in);
            }

            @Override
            public Article[] newArray(int size) {
                return new Article[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(source, flags);
            dest.writeString(author);
            dest.writeString(title);
            dest.writeString(description);
            dest.writeString(url);
            dest.writeString(urlToImage);
            dest.writeString(publishedAt);
        }

        ////////// Article Getters ////////////////////

        public Source getSource() {
            return source;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }
    }


    public static class Source implements Parcelable {
        @SerializedName("name")
        @Expose
        private String name;

        //Constructor
        public Source(String name) {
            this.name = name;
        }

        ////////// Source Parcelable ////////////////////
        private Source(Parcel in) {
            name = in.readString();
        }

        public static final Creator<Source> CREATOR = new Creator<Source>() {
            @Override
            public Source createFromParcel(Parcel in) {
                return new Source(in);
            }

            @Override
            public Source[] newArray(int size) {
                return new Source[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }

        ////////// Source Getter ////////////////////
        public String getName() {
            return name;
        }

    }
}
