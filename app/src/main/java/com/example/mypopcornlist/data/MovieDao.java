package com.example.mypopcornlist.data;

import android.content.Context;
import android.database.Cursor;

/**
 * Data Access Object for movie entries.
 */
public class MovieDao {
    private final AppDbHelper dbHelper;

    public MovieDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    /**
     * Saves a movie and returns the row ID.
     */
    public long saveMovie(String title, String description, String type, int rating, String review) {
        return dbHelper.insertMovie(title, description, type, rating, review);
    }

    /**
     * Retrieves all movies as a Cursor, ordered by newest first.
     */
    public Cursor getAllMovies() {
        return dbHelper.getAllMovies();
    }
} 