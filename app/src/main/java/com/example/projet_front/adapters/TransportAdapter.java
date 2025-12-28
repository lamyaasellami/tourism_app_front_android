package com.example.projet_front.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.activities.TransportActivity;
import com.example.projet_front.models.AccommodationProvider;
import com.example.projet_front.models.TransportProvider;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {

    private final List<TransportProvider> transport;
    private final List<TransportProvider> transportFiltered;
    private final Context context;

    public TransportAdapter(Context context,List<TransportProvider> transport) {
        this.context = context;
        this.transport = transport;
        this.transportFiltered = new ArrayList<TransportProvider>(transport);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_accommodation_card, parent, false);
        return new TransportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransportProvider transport = transportFiltered.get(position);

        // Set title
        holder.tvTitle.setText(transport.getName());

        // Set type badge
        holder.tvType.setText(transport.getType().toUpperCase());

        // Set description (truncate to 40 characters)
        String description = transport.getDescription();
        if (description != null && description.length() > 40) {
            description = description.substring(0, 40) + "...";
        }
        holder.tvDescription.setText(description);


        // Example with placeholder:
        String imageName = transport.getLogoUrl();

        if (imageName != null && !imageName.isEmpty()) {

            int imageResId = context.getResources().getIdentifier(
                    imageName,
                    "drawable",
                    context.getPackageName()
            );

            if (imageResId != 0) {
                holder.imgAccommodation.setImageResource(imageResId);
            } else {
                holder.imgAccommodation.setImageResource(R.drawable.koutoubia_placeholder);
            }

        } else {
            holder.imgAccommodation.setImageResource(R.drawable.koutoubia_placeholder);
        }


        // Handle favorite button
        holder.btnFavorite.setTag(transport.isFavorite() ? "filled" : "border");
        updateFavoriteIcon(holder.btnFavorite, transport.isFavorite());

        holder.btnFavorite.setOnClickListener(v -> {
            transport.setFavorite(!transport.isFavorite());
            updateFavoriteIcon(holder.btnFavorite, transport.isFavorite());

            String message = transport.isFavorite() ?
                    "Ajouté aux favoris" : "Retiré des favoris";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });

        // Handle card click - open website
        holder.cardAccommodation.setOnClickListener(v -> {
            String websiteUrl = transport.getWebsiteUrl();
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
        return transportFiltered.size();
    }

    private void updateFavoriteIcon(ImageView btnFavorite, boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
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
