package com.example.mypopcornlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypopcornlist.databinding.ActivityAddItemBinding;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.core.graphics.Insets;
import com.example.mypopcornlist.data.MovieDao;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private ActivityAddItemBinding binding;

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

        // Save button click – read inputs, log values, and persist
        binding.btnSave.setOnClickListener(v -> {
            String title = binding.etTitle.getText().toString().trim();
            String review = binding.etReview.getText().toString().trim();
            String type = binding.radioFilme.isChecked() ? "Filme" : "Série";
            int rating = (int) binding.ratingBar.getRating();
            Log.d(TAG, "Saving movie: title=" + title + ", type=" + type + ", rating=" + rating + ", review=" + review);
            MovieDao dao = new MovieDao(this);
            long newId = dao.saveMovie(title, "", type, rating, review);
            Log.d(TAG, "New movie row ID: " + newId);
            Intent result = new Intent();
            result.putExtra("NEW_ITEM_ID", newId);
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
