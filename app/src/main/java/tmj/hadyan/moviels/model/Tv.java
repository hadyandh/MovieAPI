package tmj.hadyan.moviels.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import tmj.hadyan.moviels.BuildConfig;

public class Tv implements Parcelable {
    int id;
    String name, photo, description, date,  score;

    public Tv(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.date = object.getString("first_air_date");
            this.name = object.getString("name");
            this.description = object.getString("overview");
            this.photo =  ("https://image.tmdb.org/t/p/w500" + object.getString("poster_path"));
            this.score = object.getString("vote_average");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Error Data", e.getMessage());
        }
    }

    public Tv() {
    }

    public Tv(int id, String name, String photo, String description, String score) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    protected Tv(Parcel in) {
        id = in.readInt();
        name = in.readString();
        photo = in.readString();
        description = in.readString();
        date = in.readString();
        score = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel in) {
            return new Tv(in);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };

    public static class TvDetail extends Tv{

        private String backdrop;
        private JSONArray genre, seasons;

        public TvDetail() {
        }

        public TvDetail(JSONObject object) {
            try {
                id = object.getInt("id");
                date = object.getString("first_air_date");
                name = object.getString("name");
                description = object.getString("overview");
                photo =  (BuildConfig.IMAGE_URL + object.getString("poster_path"));
                backdrop =  (BuildConfig.BACKDROP_URL + object.getString("backdrop_path"));
                score = object.getString("vote_average");
                seasons = object.getJSONArray("seasons");
                genre = object.getJSONArray("genres");
            } catch (Exception e){
                e.printStackTrace();
                Log.d("Error Data", e.getMessage());
            }
        }

        public JSONArray getSeasons() {
            return seasons;
        }

        public void setSeasons(JSONArray seasons) {
            this.seasons = seasons;
        }

        public String getBackdrop() {
            return backdrop;
        }

        public void setBackdrop(String backdrop) {
            this.backdrop = backdrop;
        }

        public JSONArray getGenre() {
            return genre;
        }

        public void setGenre(JSONArray genre) {
            this.genre = genre;
        }
    }
}
