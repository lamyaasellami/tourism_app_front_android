package com.example.projet_front.adapters;

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

    public PopularPlaceAdapter(List<PlaceResponse> places) {
        this.places = places;
    }

    // 🔥 AJOUT IMPORTANT
    public void updateList(List<PlaceResponse> newPlaces) {
        this.places.clear();
        this.places.addAll(newPlaces);
        notifyDataSetChanged();
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
        holder.rating.setText("★ 4.8");

        holder.image.setImageResource(R.drawable.koutoubia_placeholder);
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
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

