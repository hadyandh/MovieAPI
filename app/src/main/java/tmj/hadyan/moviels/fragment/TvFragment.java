package tmj.hadyan.moviels.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.adapter.TvAdapter;
import tmj.hadyan.moviels.model.Tv;
import tmj.hadyan.moviels.DetailTvActivity;
import tmj.hadyan.moviels.viewmodel.TvViewModel;


public class TvFragment extends Fragment {

    private TvAdapter adapter;
    private ProgressBar progressBar;
    private TvViewModel viewModel;


    public TvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TvViewModel.class);
        viewModel.getTV().observe(getViewLifecycleOwner(), getTv);

        adapter = new TvAdapter(new ArrayList<Tv>());
        adapter.notifyDataSetChanged();

        RecyclerView rvMovie = getActivity().findViewById(R.id.rv_movies);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setAdapter(adapter);

        adapter.setOnItemClickCallback(new TvAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Tv data) {
                showSelectedMovie(data);
            }
        });

        progressBar = getActivity().findViewById(R.id.progressBar);

        viewModel.setTv();

        showLoading(true);
    }

    private Observer<ArrayList<Tv>> getTv = new Observer<ArrayList<Tv>>() {
        @Override
        public void onChanged(ArrayList<Tv> tvs) {
            if (tvs != null) {
                adapter.setData(tvs);
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showSelectedMovie(Tv tv) {
        Intent moveDetail = new Intent(getActivity(), DetailTvActivity.class);
        moveDetail.putExtra(DetailTvActivity.EXTRA_TV_ID, tv.getId());
        startActivity(moveDetail);
    }

}
