package tmj.hadyan.moviels.model;

import android.util.Log;

import org.json.JSONObject;

import tmj.hadyan.moviels.BuildConfig;

public class Credit {
    String name, character, photo;

    public Credit() {
    }

    public Credit(String name, String character, String photo) {
        this.name = name;
        this.character = character;
        this.photo = photo;
    }

    public Credit(JSONObject object){
        try {
            name = object.getString("name");
            photo =  (BuildConfig.IMAGE_URL + object.getString("profile_path"));
            character = object.getString("character");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Error Data", e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
