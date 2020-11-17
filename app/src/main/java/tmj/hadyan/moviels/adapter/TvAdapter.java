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

import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.model.Tv;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.CardTvHolder> {
    private ArrayList<Tv> tvList;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public TvAdapter(ArrayList<Tv> tvList) {
        this.tvList = tvList;
    }

    public void setData(ArrayList<Tv> items) {
        tvList.clear();
        tvList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardTvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_movie, viewGroup, false);
        return new CardTvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardTvHolder holder, final int position) {
        final Tv tv = tvList.get(holder.getAdapterPosition());

        Glide.with(holder.itemView.getContext())
                .asBitmap().load(tv.getPhoto())
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

        float rating = Float.parseFloat(tv.getScore()) / 2;
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.ratingBar.setRating(rating);

        holder.movName.setText(tv.getName());
        holder.movDesc.setText(tv.getDescription());
        holder.movScore.setText(tv.getScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(tvList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvList.size();
    }

    public class CardTvHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView movName, movDesc, movScore;
        RatingBar ratingBar;

        public CardTvHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mov_poster);
            movName = itemView.findViewById(R.id.mov_name);
            movDesc = itemView.findViewById(R.id.mov_desc);
            movScore = itemView.findViewById(R.id.mov_score);
            ratingBar = itemView.findViewById(R.id.mov_rating);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Tv data);
    }
}
