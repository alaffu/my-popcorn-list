package com.example.mypopcornlist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypopcornlist.data.MovieDao;
import com.example.mypopcornlist.data.MoviesContract;
import com.example.mypopcornlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MovieDao movieDao;
    private SimpleCursorAdapter adapter;
    private Cursor cursor; // Store cursor to close it later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        movieDao = new MovieDao(this);

        // Setup ListView
        ListView listViewMovies = findViewById(R.id.listViewMovies);

        String[] fromColumns = {MoviesContract.MovieEntry.COLUMN_NAME_TITLE, MoviesContract.MovieEntry.COLUMN_NAME_RATING};
        int[] toViews = {R.id.textViewMovieTitle, R.id.textViewMovieRating};

        // Initialize with null cursor, will be swapped in onResume
        adapter = new SimpleCursorAdapter(this,
                R.layout.item_movie,
                null, // Cursor will be set in onResume
                fromColumns,
                toViews,
                0);
        listViewMovies.setAdapter(adapter);


        binding.fab.setOnClickListener(v->{
            Log.d("MainActivity", "FAB clicked - Starting AddItem activity");
            Intent intent = new Intent(this, AddItem.class);
            startActivity(intent);
            Log.d("MainActivity", "AddItem activity started");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the data in the list
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = movieDao.getAllMovies();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the cursor to avoid memory leaks
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        // If movieDao holds a reference to the database, consider adding a close() method to MovieDao/AppDbHelper
        // and call it here, e.g., movieDao.close();
    }
}