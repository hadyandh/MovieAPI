package tmj.hadyan.moviels.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import tmj.hadyan.moviels.BuildConfig;

public class Movie implements Parcelable {
    int id;
    String photo, name, description, date, score;

    public Movie() {
    }

    public Movie(int id, String photo, String name, String description, String score) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.score = score;
    }

    public Movie(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.date = object.getString("release_date");
            this.name = object.getString("title");
            this.description = object.getString("overview");
            this.photo =  (BuildConfig.IMAGE_URL + object.getString("poster_path"));
            this.score = object.getString("vote_average");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Error Data", e.getMessage());
        }
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        photo = in.readString();
        name = in.readString();
        description = in.readString();
        date = in.readString();
        score = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(photo);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(score);
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static class DetailMovie extends Movie{

        private int runtime;
        private String backdrop;
        private JSONArray genre;

        public DetailMovie() {
        }

        public DetailMovie(JSONObject object) {
            try {
                id = object.getInt("id");
                date = object.getString("release_date");
                name = object.getString("title");
                description = object.getString("overview");
                photo =  (BuildConfig.IMAGE_URL + object.getString("poster_path"));
                backdrop =  (BuildConfig.BACKDROP_URL + object.getString("backdrop_path"));
                score = object.getString("vote_average");
                runtime = object.getInt("runtime");
                genre = object.getJSONArray("genres");
            } catch (Exception e){
                e.printStackTrace();
                Log.d("Error Data", e.getMessage());
            }
        }

        public int getRuntime() {
            return runtime;
        }

        public String getBackdrop() {
            return backdrop;
        }

        public JSONArray getGenre() {
            return genre;
        }
    }
}
