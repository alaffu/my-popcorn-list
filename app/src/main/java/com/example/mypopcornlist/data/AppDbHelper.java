package com.example.mypopcornlist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mypopcornlist.data.MoviesContract;

public class AppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MyPopcornList.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + MoviesContract.MovieEntry.TABLE_NAME + " (" +
            MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoviesContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
            MoviesContract.MovieEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            MoviesContract.MovieEntry.COLUMN_NAME_TYPE + " TEXT, " +
            MoviesContract.MovieEntry.COLUMN_NAME_RATING + " INTEGER, " +
            MoviesContract.MovieEntry.COLUMN_NAME_REVIEW + " TEXT, " +
            MoviesContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " INTEGER" +
            ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME;

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now, simply drop the table and recreate. In production, you should migrate carefully.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /** Inserts a new movie entry and returns its row ID. */
    public long insertMovie(String title, String description, String type, int rating, String review) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TITLE, title);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TYPE, type);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_RATING, rating);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_REVIEW, review);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis());
        return db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
    }

    /** Returns a Cursor over all movie entries, sorted by newest first. */
    public Cursor getAllMovies() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.COLUMN_NAME_TITLE,
                MoviesContract.MovieEntry.COLUMN_NAME_DESCRIPTION,
                MoviesContract.MovieEntry.COLUMN_NAME_TYPE,
                MoviesContract.MovieEntry.COLUMN_NAME_RATING,
                MoviesContract.MovieEntry.COLUMN_NAME_REVIEW,
                MoviesContract.MovieEntry.COLUMN_NAME_TIMESTAMP
        };
        return db.query(
                MoviesContract.MovieEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                MoviesContract.MovieEntry.COLUMN_NAME_TIMESTAMP + " DESC"
        );
    }
} 