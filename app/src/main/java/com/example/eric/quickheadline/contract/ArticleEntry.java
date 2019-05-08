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

package com.example.eric.quickheadline.contract;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eric on 22/02/2018.
 * Class that defines the table contents for our News Articles
 */
public class ArticleEntry implements BaseColumns {

    /*
     * The Content authority serves as a name for the provider (Content Provider)
     */
    public static final String ARTICLE_CONTENT_AUTHORITY = "com.example.eric.quickheadline.provider.articleprovider";

    /*
     * The CONTENT_AUTHORITY is used to create the base of all URI's which apps will use to contact
     * the content provider
     */
    public static final Uri ARTICLE_BASE_CONTENT_URI = Uri.parse("content://" + ARTICLE_CONTENT_AUTHORITY);

    /*
     * paths that can be appended to the BASE_CONTENT_URI to form valid URIs
     * For instance,
     *      content://com.example.eric.quickheadline.provider.articleprovider/article/
     *      [                         BASE_CONTENT_URI                       ][PATH_ARTICLE_HEADLINE ]
     */
    public static final String PATH_ARTICLE = "article";

    /* The CONTENT_URI used to query the article database table */
    public static final Uri CONTENT_URI_ARTICLE = ARTICLE_BASE_CONTENT_URI
            .buildUpon()
            .appendPath(PATH_ARTICLE)
            .build();

    /* Defines the name of the database article table. */
    public static final String TABLE_NAME = "article";

    /*
    Column names for article headlines. All these details are stored
     * as returned by the API*/

    /* STRING: representing the  source the article came from.*/
    public static final String COLUMN_NAME_SOURCE = "source";

    /* STRING: representing the author of the article.*/
    public static final String COLUMN_NAME_AUTHOR = "author";

    /* STRING: representing the headline or title of the article.*/
    public static final String COLUMN_NAME_TITLE = "title";

    /* STRING: representing a description or snippet of the article.*/
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    /* STRING: representing the direct URL to the article.*/
    public static final String COLUMN_NAME_URL = "url";

    /* STRING: representing the URL to a relevant image of the article.*/
    public static final String COLUMN_NAME_URL_TO_IMAGE = "url_to_image";

    /* STRING: representing The date and time that the article was published, in UTC (+000)*/
    public static final String COLUMN_NAME_PUBLISHED_DATE = "published_date";




}
