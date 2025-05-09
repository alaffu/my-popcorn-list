package com.example.mypopcornlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.mypopcornlist.data.MovieDao;
import com.example.mypopcornlist.data.MoviesContract;
import com.google.android.material.snackbar.Snackbar;

public class MovieCursorAdapter extends SimpleCursorAdapter {
    public MovieCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        // Bind the title and rating using the parent SimpleCursorAdapter
        super.bindView(view, context, cursor);

        // Setup the overflow menu button
        ImageButton btnMore = view.findViewById(R.id.btnMore);
        TextView tvTitle = view.findViewById(R.id.textViewMovieTitle);
        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(MoviesContract.MovieEntry._ID));

        btnMore.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenu().add("Editar");
            menu.getMenu().add("Remover");
            menu.setOnMenuItemClickListener(item -> {
                String title = tvTitle.getText().toString();
                if ("Editar".equals(item.getTitle())) {
                    Intent intent = new Intent(context, AddItem.class);
                    intent.putExtra("ITEM_ID", id);
                    context.startActivity(intent);
                } else if ("Remover".equals(item.getTitle())) {
                    new AlertDialog.Builder(context)
                        .setTitle("Remover")
                        .setMessage("Tem certeza que deseja remover \"" + title + "\"?")
                        .setPositiveButton("Remover", (dialog, which) -> {
                            MovieDao dao = new MovieDao(context);
                            dao.deleteMovie(id);
                            if (context instanceof MainActivity) {
                                ((MainActivity) context).refreshList();
                                Snackbar.make(
                                    ((Activity) context).findViewById(R.id.main),
                                    "\"" + title + "\" removido",
                                    Snackbar.LENGTH_LONG
                                ).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
                }
                return true;
            });
            menu.show();
        });
    }
} 