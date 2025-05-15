package com.example.mypopcornlist.data;

import android.content.Context;
import android.database.Cursor;

/**
 * Objeto de Acesso a Dados para entradas de filmes.
 */
public class MovieDao {
    private final AppDbHelper dbHelper;

    public MovieDao(Context context) {
        dbHelper = new AppDbHelper(context);
    }

    /**
     * Salva um filme e retorna o ID da linha.
     */
    public long saveMovie(String title, String description, String type, int rating, String review) {
        return dbHelper.insertMovie(title, description, type, rating, review);
    }

    /**
     * Recupera todos os filmes como um Cursor, ordenados pelos mais recentes primeiro.
     */
    public Cursor getAllMovies() {
        return dbHelper.getAllMovies();
    }

    /** Recupera um único filme por ID. */
    public Cursor getMovieById(long id) {
        return dbHelper.getMovie(id);
    }

    /** Atualiza um filme e retorna o número de linhas atualizadas. */
    public int updateMovie(long id, String title, String description, String type, int rating, String review) {
        return dbHelper.updateMovie(id, title, description, type, rating, review);
    }

    /** Exclui um filme por ID. */
    public int deleteMovie(long id) {
        return dbHelper.deleteMovie(id);
    }
} 