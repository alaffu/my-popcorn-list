package com.example.mypopcornlist.data;

import android.provider.BaseColumns;

public final class MoviesContract {
    private MoviesContract() {}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_REVIEW = "review";
    }
} 