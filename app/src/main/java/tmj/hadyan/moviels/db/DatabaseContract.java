package tmj.hadyan.moviels.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NAME_MOVIE = "movie";
    static String TABLE_NAME_TV = "tv";

    public static final class MovieColumns implements BaseColumns {
        public static String ID_MOVIE = "id";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String PHOTO_PATH = "photoPath";
        public static String SCORE = "score";
    }

    public static final class TvColumns implements BaseColumns {
        public static String ID_TV = "id";
        public static String NAME = "name";
        public static String DESCRIPTION = "description";
        public static String PHOTO_PATH = "photoPath";
        public static String SCORE = "score";
    }
}
