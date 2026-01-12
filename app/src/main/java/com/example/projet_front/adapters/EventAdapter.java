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
import com.example.projet_front.models.EventProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private final List<EventProvider> events;
    //private final List<EventProvider> eventsFiltered;
    private final Context context;

    public EventAdapter(Context context, List<EventProvider> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_card, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventProvider event = events.get(position);

        // Set title
        holder.tvEventName.setText(event.getName());

        // Set event type
        holder.tvType.setText(event.getEventType().toUpperCase());

        // Set event date
        holder.tvEventDates.setText(event.getStartDate() + " - " + event.getEndDate());
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        String dates =
                event.getStartDate().format(formatter) +
                        " - " +
                        event.getEndDate().format(formatter);

        holder.tvEventDates.setText(dates);*/


        // Set event city
        holder.tvEventCity.setText(event.getCityName());

        // Set description (truncate to 40 characters)
        String description = event.getDescription();
        if (description != null && description.length() > 40) {
            description = description.substring(0, 40) + "...";
        }
        holder.tvEventDescription.setText(description);


        // Example with placeholder:
        String imageName = event.getImgPath();

        if (imageName != null && !imageName.isEmpty()) {

            int imageResId = context.getResources().getIdentifier(
                    imageName,
                    "drawable",
                    context.getPackageName()
            );

            if (imageResId != 0) {
                holder.ivEventImage.setImageResource(imageResId);
            } else {
                holder.ivEventImage.setImageResource(R.drawable.afcon2025);
            }

        } else {
            holder.ivEventImage.setImageResource(R.drawable.afcon2025);
        }


        /* Handle favorite button
        holder.btnFavorite.setTag(event.isFavorite() ? "filled" : "border");
        updateFavoriteIcon(holder.btnFavorite, event.isFavorite());

        holder.btnFavorite.setOnClickListener(v -> {
            event.setFavorite(!event.isFavorite());
            updateFavoriteIcon(holder.btnFavorite, event.isFavorite());

            String message = event.isFavorite() ?
                    "Ajouté aux favoris" : "Retiré des favoris";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });*/

        // Handle card click - open website
        holder.cardEvent.setOnClickListener(v -> {
            String websiteUrl = event.getWebsiteUrl();
            if (websiteUrl != null && !websiteUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                context.startActivity(browserIntent);
            } else {
                Toast.makeText(context, "Site web non disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle website link container click
        holder.btnVisitWebsite.setOnClickListener(v -> {
            holder.cardEvent.performClick();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardEvent;
        ImageView ivEventImage;
        //Chip chipEventType;
        TextView tvEventDates, tvEventCity, tvType;
        TextView tvEventName, tvEventDescription;

        MaterialButton btnVisitWebsite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardEvent = itemView.findViewById(R.id.cardEvent);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventDescription = itemView.findViewById(R.id.tvEventDescription);
            tvEventDates = itemView.findViewById(R.id.tvEventDates);
            tvEventCity = itemView.findViewById(R.id.tvEventCity);

            btnVisitWebsite = itemView.findViewById(R.id.btnVisitWebsite);
            tvType = itemView.findViewById(R.id.tvType);
            //chipEventType = itemView.findViewById(R.id.chipEventType);
        }
    }
}
