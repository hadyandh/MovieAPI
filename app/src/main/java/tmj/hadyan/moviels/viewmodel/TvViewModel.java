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
import tmj.hadyan.moviels.model.Tv;

public class TvViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<Tv>> listTv = new MutableLiveData<>();
    private MutableLiveData<Tv.TvDetail> detailTv = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Credit>> listCredit = new MutableLiveData<>();

    public void setTv() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Tv> tvItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject Items = list.getJSONObject(i);
                        Tv tv = new Tv(Items);
                        tvItems.add(tv);
                    }
                    listTv.postValue(tvItems);
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

    public void setDetailTv(final int id) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/tv/" +id+ "?api_key=" + API_KEY;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    Tv.TvDetail tvs = new Tv.TvDetail(responseObject);
                    detailTv.postValue(tvs);
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

    public void setCreditTv(final int id) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Credit> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/tv/" +id+ "/credits?api_key=" + API_KEY;

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

    public LiveData<ArrayList<Tv>> getTV() {
        return listTv;
    }

    public LiveData<Tv.TvDetail> getDetailTv() { return detailTv; }

    public LiveData<ArrayList<Credit>> getCredit() { return listCredit; }

}
