package com.example.projet_front.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.projet_front.R;
import com.example.projet_front.models.AccommodationProvider;

import java.util.ArrayList;
import java.util.List;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.ViewHolder> {

    private List<AccommodationProvider> accommodations;
    private List<AccommodationProvider> accommodationsFiltered;
    private Context context;

    public AccommodationAdapter(Context context, List<AccommodationProvider> accommodations) {
        this.context = context;
        this.accommodations = accommodations;
        this.accommodationsFiltered = new ArrayList<>(accommodations);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accommodation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccommodationProvider accommodation = accommodationsFiltered.get(position);

        // Set title
        holder.tvTitle.setText(accommodation.getName());

        // Set type badge
        holder.tvType.setText(accommodation.getType().toUpperCase());

        // Set description (truncate to 40 characters)
        String description = accommodation.getDescription();
        if (description != null && description.length() > 40) {
            description = description.substring(0, 40) + "...";
        }
        holder.tvDescription.setText(description);

        // Set image (you can use Glide or Picasso for loading images from URL)
        // Example with placeholder:
        if (accommodation.getLogoUrl() != null && !accommodation.getLogoUrl().isEmpty()) {
            // Glide.with(context).load(accommodation.getLogoUrl()).into(holder.imgAccommodation);
            holder.imgAccommodation.setImageResource(R.drawable.koutoubia_placeholder); // For now
        } else {
            holder.imgAccommodation.setImageResource(R.drawable.koutoubia_placeholder);
        }

        // Handle favorite button
        holder.btnFavorite.setTag(accommodation.isFavorite() ? "filled" : "border");
        updateFavoriteIcon(holder.btnFavorite, accommodation.isFavorite());

        holder.btnFavorite.setOnClickListener(v -> {
            accommodation.setFavorite(!accommodation.isFavorite());
            updateFavoriteIcon(holder.btnFavorite, accommodation.isFavorite());

            String message = accommodation.isFavorite() ?
                    "Ajouté aux favoris" : "Retiré des favoris";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });

        // Handle card click - open website
        holder.cardAccommodation.setOnClickListener(v -> {
            String websiteUrl = accommodation.getWebsiteUrl();
            if (websiteUrl != null && !websiteUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                context.startActivity(browserIntent);
            } else {
                Toast.makeText(context, "Site web non disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle website link container click
        holder.websiteLinkContainer.setOnClickListener(v -> {
            holder.cardAccommodation.performClick();
        });
    }

    @Override
    public int getItemCount() {
        return accommodationsFiltered.size();
    }

    private void updateFavoriteIcon(ImageView btnFavorite, boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    // Filter method
    public void filter(String filterType) {
        accommodationsFiltered.clear();

        if (filterType.equalsIgnoreCase("All")) {
            accommodationsFiltered.addAll(accommodations);
        } else {
            for (AccommodationProvider accommodation : accommodations) {
                if (accommodation.getType().equalsIgnoreCase(filterType)) {
                    accommodationsFiltered.add(accommodation);
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardAccommodation;
        ImageView imgAccommodation;
        ImageView btnFavorite;
        TextView tvTitle;
        TextView tvType;
        TextView tvDescription;
        View websiteLinkContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardAccommodation = itemView.findViewById(R.id.cardAccommodation);
            imgAccommodation = itemView.findViewById(R.id.imgAccommodation);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvType = itemView.findViewById(R.id.tvType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            websiteLinkContainer = itemView.findViewById(R.id.websiteLinkContainer);
        }
    }
}
