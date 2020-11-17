package tmj.hadyan.moviels.adapter;

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
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import tmj.hadyan.moviels.model.Movie;
import tmj.hadyan.moviels.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CardViewViewHolder> {
    private ArrayList<Movie> mData;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public MovieAdapter(ArrayList<Movie> mData) {
        this.mData = mData;
    }

    public void setData(ArrayList<Movie> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_movie, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, final int position) {
        final Movie movie = mData.get(holder.getAdapterPosition());

//        Glide.with(holder.itemView.getContext())
//                .load(movie.getPhoto())
//                .placeholder(R.color.colorSecondary)
//                .apply(new RequestOptions().override(350, 550))
//                .into(holder.imgPhoto);

        Glide.with(holder.itemView.getContext())
                .asBitmap().load(movie.getPhoto())
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

        float rating = Float.parseFloat(movie.getScore()) / 2;
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.ratingBar.setRating(rating);

        holder.movName.setText(movie.getName());
        holder.movDesc.setText(movie.getDescription());
        holder.movScore.setText(movie.getScore() + " / 10");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView movName, movDesc, movScore;
        RatingBar ratingBar;
        CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mov_poster);
            movName = itemView.findViewById(R.id.mov_name);
            movDesc = itemView.findViewById(R.id.mov_desc);
            movScore = itemView.findViewById(R.id.mov_score);
            ratingBar = itemView.findViewById(R.id.mov_rating);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }
}
