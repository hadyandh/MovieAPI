package tmj.hadyan.moviels.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import tmj.hadyan.moviels.DetailMovieActivity;
import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.adapter.MovieFavouriteAdapter;
import tmj.hadyan.moviels.db.MovieHelper;
import tmj.hadyan.moviels.helper.MappingHelper;
import tmj.hadyan.moviels.model.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavouriteFragment extends Fragment implements LoadMoviesCallback {
    private ProgressBar progressBar;
    private RecyclerView rvMovie;
    private MovieFavouriteAdapter adapter;
    private MovieHelper movieHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";


    public MovieFavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_favourite, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        rvMovie = view.findViewById(R.id.rv_movies);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setHasFixedSize(true);

        adapter = new MovieFavouriteAdapter(getActivity());
        rvMovie.setAdapter(adapter);

        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.open();

        /*
        Cek jika savedInstaceState null maka akan melakukan proses asynctask nya
        jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         */
        if (savedInstanceState == null) {
            new LoadMoviesAsync(movieHelper, this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListMovie(list);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMovie());
    }

    @Override
    public void preExecute() {
        /*
        Callback yang akan dipanggil di onPreExecute Asyntask
        Memunculkan progressbar
        */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        /*
        Callback yang akan dipanggil di onPostExture Asynctask
        Menyembunyikan progressbar, kemudian isi adapter dengan data yang ada
         */
        progressBar.setVisibility(View.INVISIBLE);
        if (movies.size() > 0) {
            adapter.setListMovie(movies);
        } else {
            adapter.setListMovie(new ArrayList<Movie>());
            showSnackbarMessage(getString(R.string.null_data));
        }
    }

    private class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {

        private final WeakReference<MovieHelper> weakMovieHelper;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(MovieHelper movieHelper, LoadMoviesCallback callback) {
            weakMovieHelper = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Cursor dataCursor = weakMovieHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    //Tambahkan juga onActivityResult di parent activty dari fragment ini
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            if (resultCode == DetailMovieActivity.RESULT_DELETE_MOVIE) {
                int position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0);

                adapter.removeItem(position);

                showSnackbarMessage(getString(R.string.delete_item));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }

    public void showSnackbarMessage(String message){
        Snackbar.make(rvMovie, message, Snackbar.LENGTH_SHORT).show();
    }
}

interface LoadMoviesCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
