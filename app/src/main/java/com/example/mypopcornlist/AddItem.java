package com.example.mypopcornlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.database.Cursor;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypopcornlist.databinding.ActivityAddItemBinding;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.core.graphics.Insets;
import com.example.mypopcornlist.data.MovieDao;
import com.example.mypopcornlist.data.MoviesContract;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private ActivityAddItemBinding binding;
    private boolean isEditMode = false;
    private long editItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = binding.topAppBar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewCompat.setOnApplyWindowInsetsListener(
                binding.main,
                (view, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // Check if launched for editing an existing movie
        Intent launchIntent = getIntent();
        if (launchIntent != null && launchIntent.hasExtra("ITEM_ID")) {
            isEditMode = true;
            editItemId = launchIntent.getLongExtra("ITEM_ID", -1);
            if (editItemId != -1) {
                MovieDao dao = new MovieDao(this);
                Cursor c = dao.getMovieById(editItemId);
                if (c != null && c.moveToFirst()) {
                    binding.etTitle.setText(c.getString(c.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_NAME_TITLE)));
                    binding.etReview.setText(c.getString(c.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_NAME_REVIEW)));
                    String typeValue = c.getString(c.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_NAME_TYPE));
                    if ("Filme".equals(typeValue)) binding.radioFilme.setChecked(true);
                    else binding.radioSerie.setChecked(true);
                    binding.ratingBar.setRating(c.getInt(c.getColumnIndexOrThrow(MoviesContract.MovieEntry.COLUMN_NAME_RATING)));
                }
                if (c != null) c.close();
                binding.btnSave.setText("Atualizar");
                getSupportActionBar().setTitle("Editar filme/série");
            }
        }

        // Save button click – read inputs, log values, and persist
        binding.btnSave.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String review = binding.etReview.getText().toString().trim();
            String type = binding.radioFilme.isChecked() ? "Filme" : "Série";
            int rating = (int) binding.ratingBar.getRating();
            Log.d(TAG, (isEditMode ? "Updating" : "Saving") + " movie: title=" + title + ", type=" + type + ", rating=" + rating + ", review=" + review);
            MovieDao dao = new MovieDao(this);
            Intent result = new Intent();
            if (isEditMode) {
                dao.updateMovie(editItemId, title, "", type, rating, review);
                result.putExtra("UPDATED_ITEM_ID", editItemId);
            } else {
                long newId = dao.saveMovie(title, "", type, rating, review);
                result.putExtra("NEW_ITEM_ID", newId);
            }
            setResult(RESULT_OK, result);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
