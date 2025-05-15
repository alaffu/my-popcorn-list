package com.example.mypopcornlist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypopcornlist.data.MovieDao;
import com.example.mypopcornlist.data.MoviesContract;
import com.example.mypopcornlist.databinding.ActivityMainBinding;
import com.example.mypopcornlist.MovieCursorAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MovieDao movieDao;
    private MovieCursorAdapter adapter;
    private Cursor cursor; // Armazena o cursor para fechá-lo mais tarde

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

        // Configura o ListView
        ListView listViewMovies = findViewById(R.id.listViewMovies);

        String[] fromColumns = {MoviesContract.MovieEntry.COLUMN_NAME_TITLE};
        int[] toViews = {R.id.textViewMovieTitle};

        // Inicializa com cursor nulo, será trocado no onResume
        adapter = new MovieCursorAdapter(
                this,
                R.layout.item_movie,
                null, // O cursor será definido em onResume
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
        refreshList();
    }

    /** Atualiza os dados da lista a partir do banco de dados. */
    public void refreshList() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = movieDao.getAllMovies();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fecha o cursor para evitar vazamentos de memória
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        // Se movieDao mantiver uma referência ao banco de dados, considere adicionar um método close() a MovieDao/AppDbHelper
        // e chamá-lo aqui, por exemplo, movieDao.close();
    }
}