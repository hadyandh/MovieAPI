package tmj.hadyan.moviels;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tmj.hadyan.moviels.adapter.CreditAdapter;
import tmj.hadyan.moviels.db.TvHelper;
import tmj.hadyan.moviels.model.Credit;
import tmj.hadyan.moviels.model.Tv;
import tmj.hadyan.moviels.viewmodel.TvViewModel;

public class DetailTvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_TV_ID = "extra_tv_id";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int RESULT_DELETE_TV = 202;

    private ArrayList<Credit> creditList = new ArrayList<>();
    private String[] genres;
    private boolean isFavorit = false;
    private int position;
    private int tvId;

    private TvViewModel tvViewModel;
    private TvHelper tvHelper;

    private Tv favouriteTv;
    private CreditAdapter adapter;

    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private TextView score, sinopsis, date, title, runtime;
    private ChipGroup cgGenre;
    private ImageView poster, backdrop, btnBack;
    private FloatingActionButton btnFavourite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        score = findViewById(R.id.mov_score);
        sinopsis = findViewById(R.id.mov_description);
        date = findViewById(R.id.mov_date);
        cgGenre = findViewById(R.id.mov_genre);
        title = findViewById(R.id.mov_name);
        poster = findViewById(R.id.mov_poster);
        backdrop = findViewById(R.id.mov_backdrop);
        ratingBar = findViewById(R.id.mov_rating);
        runtime = findViewById(R.id.mov_runtime);
        btnFavourite = findViewById(R.id.btn_fav);
        progressBar = findViewById(R.id.progressBarDetail);
        btnBack = findViewById(R.id.btn_back);

        showLoading(true);

        Bundle extra = getIntent().getExtras();
        tvId = extra.getInt(EXTRA_TV_ID);
        position = extra.getInt(EXTRA_POSITION);

        tvViewModel = new ViewModelProvider(this).get(TvViewModel.class);
        tvViewModel.getDetailTv().observe(this, getTv);
        tvViewModel.getCredit().observe(this, getCredit);

        tvViewModel.setCreditTv(tvId);
        tvViewModel.setDetailTv(tvId);

        adapter = new CreditAdapter(new ArrayList<Credit>());
        adapter.notifyDataSetChanged();

        RecyclerView rvCredit = findViewById(R.id.rv_credit);
        rvCredit.setHasFixedSize(true);
        rvCredit.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCredit.setAdapter(adapter);

        btnFavourite.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void setTv(Tv.TvDetail tvDetail) {
        favouriteTv = tvDetail;
        tvHelper = TvHelper.getInstance(getApplicationContext());
        tvHelper.open();

        Glide.with(this).load(tvDetail.getBackdrop()).into(backdrop);

        Glide.with(this)
                .asBitmap().load(tvDetail.getPhoto())
                .centerCrop()
                .into(new BitmapImageViewTarget(poster) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable rounded =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        rounded.setCornerRadius(16);
                        poster.setImageDrawable(rounded);
                    }
                });

        float rating = Float.parseFloat(tvDetail.getScore()) / 2;
        if (rating < 1) { rating = 1; }

        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setRating(rating);

        title.setText(tvDetail.getName());
        sinopsis.setText(tvDetail.getDescription());
        date.setText(tvDetail.getDate());
        score.setText(tvDetail.getScore());
        runtime.setText(String.valueOf(tvDetail.getSeasons().length()) + " Season"); //count length from JSON Array

        genres = toStringArray(tvDetail.getGenre());
        for (String genre : genres) {
            Chip chip = new Chip(this);
            chip.setText(genre);
            cgGenre.addView(chip);
        }

        favouriteState(tvId);
    }

    private void insertData() {
        long result = tvHelper.insert(favouriteTv);

        if (result > 0) {
            Toast.makeText(DetailTvActivity.this, getString(R.string.success), Toast.LENGTH_LONG).show();
            btnFavourite.setImageResource(R.drawable.ic_favorite);
            isFavorit = true;
        } else {
            Toast.makeText(DetailTvActivity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteData() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        setResult(RESULT_DELETE_TV, intent);

        long resultMovie = tvHelper.deleteById(String.valueOf(tvId));

        if (resultMovie > 0){
            isFavorit = false;

            btnFavourite.setImageResource(R.drawable.ic_favorite_border);
            Toast.makeText(DetailTvActivity.this, getString(R.string.delete_item), Toast.LENGTH_SHORT).show();
        }
    }

    private Observer<ArrayList<Credit>> getCredit = new Observer<ArrayList<Credit>>() {
        @Override
        public void onChanged(ArrayList<Credit> credit) {
            if (credit != null) {
                creditList = credit;
                adapter.setData(credit);
            }
        }
    };

    private Observer<Tv.TvDetail> getTv = new Observer<Tv.TvDetail>() {
        @Override
        public void onChanged(Tv.TvDetail tvs) {
            if (tvs != null) {
                setTv(tvs);
                showLoading(false);
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_fav) {
            if (!isFavorit){
                insertData();
            } else {
                deleteData();
            }

        } else if (view.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    private void favouriteState(int id) {
        Cursor resultUser = tvHelper.queryById(String.valueOf(id));
        if (resultUser.getCount() > 0) {
            isFavorit = true;
            btnFavourite.setImageResource(R.drawable.ic_favorite);
        }
    }

    public static String[] toStringArray(JSONArray array) {
        if (array == null)
            return null;

        String[] arr = new String[array.length()];
        for (int i = 0; i < arr.length; i++) {
            try {
                JSONObject item = array.getJSONObject(i);//gets the ith Json object of JSONArray
                String name = item.getString("name");
                arr[i] = name;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvHelper.close();
    }
}
