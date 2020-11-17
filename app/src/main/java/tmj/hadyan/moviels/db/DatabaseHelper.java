package tmj.hadyan.moviels.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbfavourite";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME_MOVIE,
            DatabaseContract.MovieColumns.ID_MOVIE,
            DatabaseContract.MovieColumns.TITLE,
            DatabaseContract.MovieColumns.DESCRIPTION,
            DatabaseContract.MovieColumns.SCORE,
            DatabaseContract.MovieColumns.PHOTO_PATH
    );

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME_TV,
            DatabaseContract.TvColumns.ID_TV,
            DatabaseContract.TvColumns.NAME,
            DatabaseContract.TvColumns.DESCRIPTION,
            DatabaseContract.TvColumns.SCORE,
            DatabaseContract.TvColumns.PHOTO_PATH
    );

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME_TV);
        onCreate(db);
    }
}
