package tmj.hadyan.moviels.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import tmj.hadyan.moviels.BuildConfig;
import tmj.hadyan.moviels.model.Credit;
import tmj.hadyan.moviels.model.Movie;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();
    private MutableLiveData<Movie.DetailMovie> detailMovie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Credit>> listCredit = new MutableLiveData<>();
    private String TAG = "ViewModel";

    public void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movieItems = list.getJSONObject(i);
                        Movie movie = new Movie(movieItems);
                        listItems.add(movie);
                    }
                    listMovie.postValue(listItems);
                } catch (Exception e) {
                    Log.d("onFailure", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void setDetailMovie(final int id) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/" +id+ "?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    Movie.DetailMovie movie = new Movie.DetailMovie(responseObject);
                    detailMovie.postValue(movie);
                } catch (Exception e) {
                    Log.d("onFailure", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void setCreditMovie(final int id) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Credit> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/" +id+ "/credits?api_key=" + API_KEY;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("cast");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject items = list.getJSONObject(i);
                        Credit credit = new Credit(items);
                        listItems.add(credit);
                    }
                    listCredit.postValue(listItems);
                } catch (Exception e) {
                    Log.d("onFailure", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Movie>> getMovie() { return listMovie; }

    public LiveData<Movie.DetailMovie> getDetailMovie() { return detailMovie; }

    public LiveData<ArrayList<Credit>> getCredit() { return listCredit; }
}
