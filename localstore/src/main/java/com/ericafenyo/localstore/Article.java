package com.ericafenyo.localstore;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "article")
public class Article {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String section;
    private String description;
    @ColumnInfo(name = "image_url") private String imageUrl;
    @ColumnInfo(name = "article_url") private String articleUrl;
    @ColumnInfo(name = "published_date") private String publishedDate;
    private String author;
}
