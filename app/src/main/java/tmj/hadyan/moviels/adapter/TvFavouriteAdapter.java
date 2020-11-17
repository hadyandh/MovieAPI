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
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import tmj.hadyan.moviels.CustomOnItemClickListener;
import tmj.hadyan.moviels.DetailTvActivity;
import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.model.Tv;

public class TvFavouriteAdapter extends RecyclerView.Adapter<TvFavouriteAdapter.TvFavHolder> {
    private final ArrayList<Tv> listTv = new ArrayList<>();
    private final Activity activity;

    public TvFavouriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Tv> getListTv() {
        return listTv;
    }

    public void setListTv(ArrayList<Tv> listTv) {

        if (listTv.size() > 0) {
            this.listTv.clear();
        }
        this.listTv.addAll(listTv);

        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.listTv.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listTv.size());
    }

    @NonNull
    @Override
    public TvFavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new TvFavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvFavHolder holder, int position) {

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(listTv.get(position).getPhoto())
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

        float rating = Float.parseFloat(listTv.get(position).getScore()) / 2;
        holder.ratingBar.setVisibility(View.VISIBLE);
        holder.ratingBar.setRating(rating);

        holder.movName.setText(listTv.get(position).getName());
        holder.movDesc.setText(listTv.get(position).getDescription());
        holder.movScore.setText(listTv.get(position).getScore());

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailTvActivity.class);
                intent.putExtra(DetailTvActivity.EXTRA_POSITION, position);
                intent.putExtra(DetailTvActivity.EXTRA_TV_ID, listTv.get(position).getId());
                activity.startActivityForResult(intent, position);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listTv.size();
    }

    public class TvFavHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView movName, movDesc, movScore;
        RatingBar ratingBar;

        public TvFavHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mov_poster);
            movName = itemView.findViewById(R.id.mov_name);
            movDesc = itemView.findViewById(R.id.mov_desc);
            movScore = itemView.findViewById(R.id.mov_score);
            ratingBar = itemView.findViewById(R.id.mov_rating);
        }
    }
}
