package tmj.hadyan.moviels.helper;

import android.database.Cursor;

import java.util.ArrayList;

import tmj.hadyan.moviels.db.DatabaseContract;
import tmj.hadyan.moviels.model.Movie;
import tmj.hadyan.moviels.model.Tv;

public class MappingHelper  {
    public static ArrayList<Movie> mapCursorToArrayList(Cursor moviesCursor) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.ID_MOVIE));
            String name = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.TITLE));
            String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESCRIPTION));
            String score = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.SCORE));
            String photo = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.PHOTO_PATH));

            moviesList.add(new Movie(id, photo, name, description, score));
        }
        return moviesList;
    }

    public static ArrayList<Tv> mapCursorToArrayListTv(Cursor tvCursor) {
        ArrayList<Tv> tvList = new ArrayList<>();
        while (tvCursor.moveToNext()) {
            int id = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.ID_TV));
            String name = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.NAME));
            String description = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.DESCRIPTION));
            String score = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.SCORE));
            String photo = tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvColumns.PHOTO_PATH));

            tvList.add(new Tv(id, name, photo, description, score));
        }
        return tvList;
    }
}
