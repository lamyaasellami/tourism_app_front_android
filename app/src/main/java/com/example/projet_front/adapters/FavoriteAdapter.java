package com.example.projet_front.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.models.FavoriteResponse;
import com.example.projet_front.models.PlaceResponse;
import com.example.projet_front.models.TransportProvider;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<FavoriteResponse> favorites;

    public FavoriteAdapter(Context context, List<FavoriteResponse> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteResponse fav = favorites.get(position);

        if ("PLACE".equals(fav.getEntityType())) {
            PlaceResponse place = fav.getPlace();
            holder.title.setText(place.getName());
            holder.details.setText(place.getDescription() != null ? place.getDescription() : place.getPlaceType());
            holder.rating.setText("★ " + (place.getMinPrice() != null ? place.getMinPrice() : ""));

            // Placeholder image (replace with Glide/Picasso if you have URLs)
            holder.image.setImageResource(R.drawable.koutoubia_placeholder);
        } else if ("ACCOMMODATION".equals(fav.getEntityType())) {
            AccommodationProvider acc = fav.getAccommodation();
            holder.title.setText(acc.getName());
            holder.details.setText(acc.getType() + " • " + acc.getDescription());
            holder.rating.setText(acc.getWebsiteUrl() != null ? acc.getWebsiteUrl() : "");

            holder.image.setImageResource(R.drawable.koutoubia_placeholder);
            acc.setFavorite(true);
        } else if ("TRANSPORT".equals(fav.getEntityType())) {
            TransportProvider transport = fav.getTransport();
            holder.title.setText(transport.getType());
            holder.details.setText("Transport service");
            holder.rating.setText("");
            holder.image.setImageResource(R.drawable.koutoubia_placeholder);
        }
        /*switch (fav.getEntityType()) {
            case "PLACE" -> {
                PlaceResponse place = fav.getPlace();
                holder.title.setText(place.getName());
                holder.details.setText(place.getDescription() != null ? place.getDescription() : place.getPlaceType());
                holder.rating.setText("★ " + (place.getMinPrice() != null ? place.getMinPrice() : ""));

                // Placeholder image (replace with Glide/Picasso if you have URLs)
                holder.image.setImageResource(R.drawable.koutoubia_placeholder);
            }
            case "ACCOMMODATION" -> {
                AccommodationProvider acc = fav.getAccommodation();
                holder.title.setText(acc.getName());
                holder.details.setText(acc.getType() + " • " + acc.getDescription());
                holder.rating.setText(acc.getWebsiteUrl() != null ? acc.getWebsiteUrl() : "");

                holder.image.setImageResource(R.drawable.koutoubia_placeholder);
            }
            case "TRANSPORT" -> {
                TransportProvider transport = fav.getTransport();
                holder.title.setText(transport.getType());
                holder.details.setText("Transport service");
                holder.rating.setText("");
                holder.image.setImageResource(R.drawable.koutoubia_placeholder);
            }
        }*/

        // Favorite icon toggle
        holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        holder.favoriteIcon.setOnClickListener(v -> {
            // TODO: handle favorite toggle (API call or local update)
            Toast.makeText(context, "Clicked favorite for " + fav.getEntityType(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favoriteIcon;
        TextView title, details, rating;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.popular_image);
            title = itemView.findViewById(R.id.popular_title);
            details = itemView.findViewById(R.id.popular_details);
            rating = itemView.findViewById(R.id.popular_rating);
            favoriteIcon = itemView.findViewById(R.id.popular_favorite);
        }
    }
}

