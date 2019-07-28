package com.ericafenyo.data.entities;

public class Article {
    private String title;
    private String section;
    private String description;
    private String imageUrl;
    private String articleUrl;
    private String publishedDate;
    private String author;

    public Article(String title, String section, String description, String imageUrl,
        String articleUrl, String publishedDate, String author) {
        this.title = title;
        this.section = section;
        this.description = description;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
        this.publishedDate = publishedDate;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthor() {
        return author;
    }

    public static final class Builder {

        private String title;
        private String section;
        private String description;
        private String imageUrl;
        private String articleUrl;
        private String publishedDate;
        private String author;

        public Builder() {}

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder section(String section) {
            this.section = section;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder articleUrl(String articleUrl) {
            this.articleUrl = articleUrl;
            return this;
        }

        public Builder publishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Article build() {
            return new Article(
                title,
                section,
                description,
                imageUrl,
                articleUrl,
                publishedDate,
                author
            );
        }
    }
}
