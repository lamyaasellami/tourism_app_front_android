package com.example.projet_front.adapters;

import static java.lang.Character.toUpperCase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.models.FavoriteProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private final List<FavoriteProvider> favorites;
    private final Context context;
    public FavoriteAdapter(Context context, List<FavoriteProvider> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        FavoriteProvider favorite = favorites.get(position);

        if ("PLACE".equals(favorite.getEntityType())) {
            // Handle Place entity
            // You can access favorite.getPlace() here
            holder.title.setText(favorite.getPlace().getName());

            //holder.tvType.setText(favorite.getPlace().getPlaceType().toUpperCase());

            holder.details.setText(favorite.getPlace().getDescription());

            //holder.tvFavDate.setText(favorite.getPlace().getOpeningHours());

            //holder.tvFavCity.setText("City not available");

            holder.rating.setText("★ 4.8");

            // ❤️ État initial
            updateHeartIcon(holder.favorite, favorite.getPlace().isFavorite());

            // Handle favorite button
            holder.favorite.setTag(favorite.getPlace().isFavorite() ? "filled" : "border");
            updateHeartIcon(holder.favorite, favorite.getPlace().isFavorite());

            holder.cVfavoriteContainer.setOnClickListener(v -> {
                favorite.getPlace().setFavorite(!favorite.getPlace().isFavorite());
                updateHeartIcon(holder.favorite, favorite.getPlace().isFavorite());

                String message = favorite.getPlace().isFavorite() ?
                        "Ajouté aux favoris" : "Retiré des favoris";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        }
        // Additional entity types (e.g., ACCOMMODATION, TRANSPORT) can be handled here
        else if ("ACCOMMODATION".equals(favorite.getEntityType())) {
            holder.title.setText(favorite.getAccommodation().getName());

            //holder.tvType.setText(favorite.getAccommodation().getType().toUpperCase());

            holder.details.setText(favorite.getAccommodation().getDescription());

            //holder.tvFavCity.setText("City not available");

            holder.rating.setText("★ 4.8");

            // ❤️ État initial
            updateHeartIcon(holder.favorite, true);

            // Example with placeholder:
            String imageName = favorite.getAccommodation().getLogoUrl();

            if (imageName != null && !imageName.isEmpty()) {

                int imageResId = context.getResources().getIdentifier(
                        imageName,
                        "drawable",
                        context.getPackageName()
                );

                if (imageResId != 0) {
                    holder.image.setImageResource(imageResId);
                } else {
                    holder.image.setImageResource(R.drawable.afcon2025);
                }

            } else {
                holder.image.setImageResource(R.drawable.afcon2025);
            }

            // Handle favorite button
            holder.favorite.setTag(favorite.getAccommodation().isFavorite() ? "filled" : "border");
            updateHeartIcon(holder.favorite, favorite.getAccommodation().isFavorite());

            holder.cVfavoriteContainer.setOnClickListener(v -> {
                favorite.getAccommodation().setFavorite(!favorite.getAccommodation().isFavorite());
                updateHeartIcon(holder.favorite, favorite.getAccommodation().isFavorite());

                String message = favorite.getAccommodation().isFavorite() ?
                        "Ajouté aux favoris" : "Retiré des favoris";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });

        }

    }
    private void updateHeartIcon(ImageView heart, boolean isFavorite) {
        if (isFavorite) {
            heart.setImageResource(R.drawable.ic_favorite_filled);
            heart.setColorFilter(
                    ContextCompat.getColor(context, R.color.red));
        } else {
            heart.setImageResource(R.drawable.ic_heart_outline);
            heart.setColorFilter(
                    ContextCompat.getColor(context, R.color.gris_texte));
        }
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favorite;
        TextView title, details, rating;
        MaterialCardView cVfavoriteContainer;

        FavViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.popular_image);
            favorite = itemView.findViewById(R.id.popular_favorite);
            cVfavoriteContainer = itemView.findViewById(R.id.favoriteContainer);
            title = itemView.findViewById(R.id.popular_title);
            details = itemView.findViewById(R.id.popular_details);
            rating = itemView.findViewById(R.id.popular_rating);
        }
    }
}
