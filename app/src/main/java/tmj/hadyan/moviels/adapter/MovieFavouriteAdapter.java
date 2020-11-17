package tmj.hadyan.moviels.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import tmj.hadyan.moviels.CustomOnItemClickListener;
import tmj.hadyan.moviels.DetailMovieActivity;
import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.model.Movie;

public class MovieFavouriteAdapter extends RecyclerView.Adapter<MovieFavouriteAdapter.MovieFavouriteViewHolder> {
    private final ArrayList<Movie> listMovie = new ArrayList<>();
    private final Activity activity;

    public MovieFavouriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> listMovie) {

        if (listMovie.size() > 0) {
            this.listMovie.clear();
        }
        this.listMovie.addAll(listMovie);

        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovie.size());
    }

    @NonNull
    @Override
    public MovieFavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new MovieFavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieFavouriteViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(listMovie.get(position).getPhoto())
                .placeholder(R.color.colorSecondary)
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(listMovie.get(position).getPhoto())
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.imgPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable rounded =
                                RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                        rounded.setCornerRadius(16);
                        holder.imgPhoto.setImageDrawable(rounded);
                    }
                });

        float rating = Float.parseFloat(listMovie.get(position).getScore()) / 2;
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.ratingBar.setRating(rating);

        holder.movName.setText(listMovie.get(position).getName());
        holder.movDesc.setText(listMovie.get(position).getDescription());
        holder.movScore.setText(listMovie.get(position).getScore());

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, listMovie.get(position).getId());
                activity.startActivityForResult(intent, position);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public class MovieFavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView movName, movDesc, movScore;
        RatingBar ratingBar;

        public MovieFavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mov_poster);
            movName = itemView.findViewById(R.id.mov_name);
            movDesc = itemView.findViewById(R.id.mov_desc);
            movScore = itemView.findViewById(R.id.mov_score);
            ratingBar = itemView.findViewById(R.id.mov_rating);
        }
    }
}
