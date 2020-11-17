package tmj.hadyan.moviels.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import tmj.hadyan.moviels.model.Movie;

import static tmj.hadyan.moviels.db.DatabaseContract.MovieColumns.DESCRIPTION;
import static tmj.hadyan.moviels.db.DatabaseContract.MovieColumns.ID_MOVIE;
import static tmj.hadyan.moviels.db.DatabaseContract.MovieColumns.PHOTO_PATH;
import static tmj.hadyan.moviels.db.DatabaseContract.MovieColumns.SCORE;
import static tmj.hadyan.moviels.db.DatabaseContract.MovieColumns.TITLE;

import static tmj.hadyan.moviels.db.DatabaseContract.TABLE_NAME_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME_MOVIE;
    private static DatabaseHelper dataBaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                ID_MOVIE + " ASC");
    }

    public Cursor queryById(String id){
        return database.query(DATABASE_TABLE, null
                , ID_MOVIE + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public long insert(Movie movie){
        ContentValues args = new ContentValues();
        args.put(ID_MOVIE, movie.getId());
        args.put(TITLE, movie.getName());
        args.put(DESCRIPTION, movie.getDescription());
        args.put(SCORE, movie.getScore());
        args.put(PHOTO_PATH, movie.getPhoto());
        Log.d("INSERT", args.toString());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, ID_MOVIE + " = ?", new String[]{id});
    }
}
