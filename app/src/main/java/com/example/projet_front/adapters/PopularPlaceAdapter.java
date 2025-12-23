package com.example.projet_front.adapters;
import com.example.projet_front.interfaces.OnPlaceClickListener;
import com.example.projet_front.activities.PlaceDetailActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.models.PlaceResponse;

import java.util.List;
public class PopularPlaceAdapter
        extends RecyclerView.Adapter<PopularPlaceAdapter.ViewHolder> {

    private List<PlaceResponse> places;
    private OnPlaceClickListener listener;


    public PopularPlaceAdapter(List<PlaceResponse> places,
                               OnPlaceClickListener listener) {
        this.places = places;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceResponse place = places.get(position);

        holder.title.setText(place.getName());
        holder.details.setText(place.getPlaceType());
        holder.rating.setText("★ 4.8"); // temporaire

        // Image (plus tard avec Glide/Picasso)
        holder.image.setImageResource(R.drawable.koutoubia_placeholder);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlaceDetailActivity.class);
            intent.putExtra("place_id", place.getPlaceId());
            v.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favorite;
        TextView title, details, rating;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.popular_image);
            favorite = itemView.findViewById(R.id.popular_favorite);
            title = itemView.findViewById(R.id.popular_title);
            details = itemView.findViewById(R.id.popular_details);
            rating = itemView.findViewById(R.id.popular_rating);
        }
    }
}

