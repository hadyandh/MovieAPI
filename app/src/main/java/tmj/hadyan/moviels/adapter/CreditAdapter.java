package tmj.hadyan.moviels.adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import tmj.hadyan.moviels.R;
import tmj.hadyan.moviels.model.Credit;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CreditViewHolder> {

    private ArrayList<Credit> mData;

    public CreditAdapter(ArrayList<Credit> mData) {
        this.mData = mData;
    }

    public void setData(ArrayList<Credit> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreditViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cast, viewGroup, false);
        return new CreditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CreditViewHolder holder, int position) {
        Credit credit = mData.get(holder.getAdapterPosition());

        Glide.with(holder.itemView.getContext())
                .asBitmap().load(credit.getPhoto())
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.movPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable rounded =
                                RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                        rounded.setCornerRadius(16);
                        holder.movPhoto.setImageDrawable(rounded);
                    }
                });

        holder.movName.setText(credit.getName());
        holder.movCharacter.setText(credit.getCharacter());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder  {

        TextView movName, movCharacter;
        ImageView movPhoto;

        public CreditViewHolder(@NonNull View itemView) {
            super(itemView);

            movName = itemView.findViewById(R.id.mov_name);
            movCharacter = itemView.findViewById(R.id.mov_character);
            movPhoto = itemView.findViewById(R.id.mov_photo);
        }
    }
}
