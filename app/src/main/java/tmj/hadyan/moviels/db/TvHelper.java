package tmj.hadyan.moviels.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import tmj.hadyan.moviels.model.Tv;

import static tmj.hadyan.moviels.db.DatabaseContract.TvColumns.DESCRIPTION;
import static tmj.hadyan.moviels.db.DatabaseContract.TvColumns.ID_TV;
import static tmj.hadyan.moviels.db.DatabaseContract.TvColumns.PHOTO_PATH;
import static tmj.hadyan.moviels.db.DatabaseContract.TvColumns.SCORE;
import static tmj.hadyan.moviels.db.DatabaseContract.TvColumns.NAME;
import static tmj.hadyan.moviels.db.DatabaseContract.TABLE_NAME_TV;

public class TvHelper {
    private static final String DATABASE_TABLE = TABLE_NAME_TV;
    private static DatabaseHelper dataBaseHelper;
    private static TvHelper INSTANCE;
    private static SQLiteDatabase database;

    private TvHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static TvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvHelper(context);
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
                ID_TV + " ASC");
    }

    public Cursor queryById(String id){
        return database.query(DATABASE_TABLE, null
                , ID_TV + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public long insert(Tv favouriteTv) {
        ContentValues args = new ContentValues();
        args.put(ID_TV, favouriteTv.getId());
        args.put(NAME, favouriteTv.getName());
        args.put(DESCRIPTION, favouriteTv.getDescription());
        args.put(SCORE, favouriteTv.getScore());
        args.put(PHOTO_PATH, favouriteTv.getPhoto());
        Log.d("INSERT", args.toString());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, ID_TV + " = ?", new String[]{id});
    }
}
