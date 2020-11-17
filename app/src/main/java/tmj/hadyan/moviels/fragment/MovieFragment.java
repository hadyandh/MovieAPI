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

import tmj.hadyan.moviels.DetailMovieActivity;
import tmj.hadyan.moviels.viewmodel.MovieViewModel;
import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.adapter.MovieAdapter;
import tmj.hadyan.moviels.model.Movie;


public class MovieFragment extends Fragment implements View.OnClickListener{

    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private MovieViewModel viewModel;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.getMovie().observe(getViewLifecycleOwner(), getMovie);

        adapter = new MovieAdapter(new ArrayList<Movie>());
        adapter.notifyDataSetChanged();

        RecyclerView rvMovie = getActivity().findViewById(R.id.rv_movies);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovie.setAdapter(adapter);

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                showSelectedMovie(data);
            }
        });

        progressBar = getActivity().findViewById(R.id.progressBar);

        viewModel.setMovie();

        showLoading(true);
    }

    private Observer<ArrayList<Movie>> getMovie = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> movies) {
            if (movies != null) {
                adapter.setData(movies);
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

    private void showSelectedMovie(Movie movie) {
        Intent moveDetail = new Intent(getActivity(), DetailMovieActivity.class);
        moveDetail.putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, movie.getId());
        startActivity(moveDetail);
    }

    @Override
    public void onClick(View view) {

    }

}
