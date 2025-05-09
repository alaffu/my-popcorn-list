package com.example.mypopcornlist.data;

public class Movie {
    private final long id;
    private final String title;
    private final String description;
    private final String type;
    private final int rating;
    private final String review;
    private final long timestamp;

    public Movie(long id, String title, String description, String type, int rating, String review, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.rating = rating;
        this.review = review;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public long getTimestamp() {
        return timestamp;
    }
} 