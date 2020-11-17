package tmj.hadyan.moviels.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.fragment.FavouriteFragment;
import tmj.hadyan.moviels.fragment.MovieFavouriteFragment;
import tmj.hadyan.moviels.fragment.TvFavouriteFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final FavouriteFragment mContext;

    public SectionsPagerAdapter(FavouriteFragment context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.title_movies,
            R.string.title_tv
    };

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MovieFavouriteFragment();
                break;
            case 1:
                fragment = new TvFavouriteFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
