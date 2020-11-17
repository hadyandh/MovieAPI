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

import tmj.hadyan.moviels.DetailTvActivity;
import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.adapter.TvFavouriteAdapter;
import tmj.hadyan.moviels.db.TvHelper;
import tmj.hadyan.moviels.helper.MappingHelper;
import tmj.hadyan.moviels.model.Tv;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFavouriteFragment extends Fragment implements LoadTvCallback {
    private ProgressBar progressBar;
    private RecyclerView rvTv;
    private TvFavouriteAdapter adapter;
    private TvHelper tvHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";


    public TvFavouriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_favourite, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        rvTv = view.findViewById(R.id.rv_movies);
        rvTv.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTv.setHasFixedSize(true);

        adapter = new TvFavouriteAdapter(getActivity());
        rvTv.setAdapter(adapter);

        tvHelper = TvHelper.getInstance(getContext());
        tvHelper.open();

        /*
        Cek jika savedInstaceState null makan akan melakukan proses asynctask nya
        jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         */
        if (savedInstanceState == null) {
            new LoadTvAsync(tvHelper, this).execute();
        } else {
            ArrayList<Tv> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setListTv(list);
            }
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTv());
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Tv> tvs) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tvs.size() > 0) {
            adapter.setListTv(tvs);
        } else {
            adapter.setListTv(new ArrayList<Tv>());
        }
    }

    private class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<Tv>> {

        private final WeakReference<TvHelper> weakNoteHelper;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync (TvHelper tvHelper, LoadTvCallback callback) {
            weakNoteHelper = new WeakReference<>(tvHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Tv> doInBackground(Void... voids) {
            Cursor dataCursor = weakNoteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayListTv(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Tv> tvs) {
            super.onPostExecute(tvs);
            weakCallback.get().postExecute(tvs);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (resultCode == DetailTvActivity.RESULT_DELETE_TV) {
                int position = data.getIntExtra(DetailTvActivity.EXTRA_POSITION, 0);

                adapter.removeItem(position);

                showSnackbarMessage(getString(R.string.delete_item));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tvHelper.close();
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvTv, message, Snackbar.LENGTH_SHORT).show();
    }
}


interface LoadTvCallback {
    void preExecute();
    void postExecute(ArrayList<Tv> tvs);
}
