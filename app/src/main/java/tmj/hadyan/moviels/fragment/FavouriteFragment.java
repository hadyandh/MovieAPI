package tmj.hadyan.moviels.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.adapter.SectionsPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {
    private RecyclerView rvMovie;


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SectionsPagerAdapter PageAdapter = new SectionsPagerAdapter(this, getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(PageAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

