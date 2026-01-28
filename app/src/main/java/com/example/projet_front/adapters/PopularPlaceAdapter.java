package com.example.projet_front.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;
import com.example.projet_front.activities.PlaceDetailActivity;
import com.example.projet_front.api.ApiClient;
import com.example.projet_front.api.ApiService;
import com.example.projet_front.models.FavoriteRequest;
import com.example.projet_front.models.PlaceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularPlaceAdapter
        extends RecyclerView.Adapter<PopularPlaceAdapter.ViewHolder> {

    private Context context;
    private List<PlaceResponse> places;
    private ApiService api;
    private int currentUserId = 3;
    private OnPlaceClickListener listener;

    // ================= INTERFACE =================
    public interface OnPlaceClickListener {
        void onPlaceClick(PlaceResponse place);
    }

    // ================= CONSTRUCTEUR 1 =================
    // Pour PlaceActivity, FavoritesFragment
    public PopularPlaceAdapter(Context context, List<PlaceResponse> places) {
        this.context = context;
        this.places = places;
        this.api = ApiClient.getClient().create(ApiService.class);
        this.listener = null;
    }

    // ================= CONSTRUCTEUR 2 =================
    // Pour HomeFragment (clic personnalisé)
    public PopularPlaceAdapter(Context context,
                               List<PlaceResponse> places,
                               OnPlaceClickListener listener) {
        this.context = context;
        this.places = places;
        this.api = ApiClient.getClient().create(ApiService.class);
        this.listener = listener;
    }

    // ================= UPDATE LIST =================
    public void updateList(List<PlaceResponse> newList) {
        this.places = newList;
        notifyDataSetChanged();
    }

    // ================= VIEW HOLDER =================
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.popular_item, parent, false);
        return new ViewHolder(view);
    }

    // ================= BIND =================
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PlaceResponse place = places.get(position);
        Log.d("ADAPTER", "Bind: " + place.getName());

        holder.title.setText(place.getName());
        holder.details.setText(place.getPlaceType());
        holder.rating.setText("★ 4.8");

        // ❤️ État initial
        updateHeartIcon(holder.favorite, place.isFavorite());
        // Force favorite state in Favorites screen
        /*place.setFavorite(true);
        updateHeartIcon(holder.favorite, true);
*/

        // 🔍 CLIC ITEM
        // Image (plus tard avec Glide/Picasso)
//        holder.image.setImageResource(R.drawable.koutoubia_placeholder);
        int imageRes = getImageByType(place.getPlaceType());
        holder.image.setImageResource(imageRes);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlaceClick(place);
            } else {
                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra("place_id", place.getPlaceId());
                context.startActivity(intent);
            }
        });

        // ❤️ CLIC FAVORI
        /*holder.favorite.setOnClickListener(v -> {
            FavoriteRequest request =
                    new FavoriteRequest(currentUserId, place.getPlaceId());

            if (!place.isFavorite()) {
                api.addFavorite(request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call,
                                           Response<Void> response) {
                        if (response.isSuccessful()) {
                            place.setFavorite(true);
                            updateHeartIcon(holder.favorite, true);
                            Toast.makeText(context,
                                    "Ajouté aux favoris", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context,
                                "Erreur ajout favori", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                api.removeFavorite(request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call,
                                           Response<Void> response) {
                        if (response.isSuccessful()) {
                            place.setFavorite(false);
                            updateHeartIcon(holder.favorite, false);
                            Toast.makeText(context,
                                    "Retiré des favoris", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context,
                                "Erreur suppression favori", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }

    // ================= HEART =================
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
        return places != null ? places.size() : 0;
    }

    // ================= HOLDER =================
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
    private int getImageByType(String type) {
        if (type == null) return R.drawable.koutoubia_placeholder;

        switch (type.toLowerCase()) {
            case "monument":
                return R.drawable.ic_monument;

            case "parc":
                return R.drawable.ic_park;

            case "marché":
            case "marche":
                return R.drawable.ic_market;

            case "divertissement":
                return R.drawable.ic_entertainment;

            case "musée":
            case "musee":
                return R.drawable.ic_museum;

            case "jardin":
                return R.drawable.ic_garden;

            case "restauration":
                return R.drawable.ic_restaurant2;

            case "mosquée":
            case "mosquee":
                return R.drawable.koutoubia_placeholder;

            case "popular":
                return R.drawable.ic_popular;

            default:
                return R.drawable.koutoubia_placeholder;
        }
    }

}
