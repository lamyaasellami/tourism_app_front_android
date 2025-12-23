package com.example.projet_front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_front.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private int[] images = {R.drawable.moroccantag, R.drawable.zelij, R.drawable.maroc_combo};
    private String[] titles = {"Bienvenue", "Explorez", "Réservez"};
    private String[] descriptions = {"Explorez les plus beaux paysages et monuments historiques.",
            "Trouvez les meilleures adresses : Riads authentiques, restaurants locaux et joyaux cachés.",
            "Planifiez votre voyage en un clic et profitez d'expériences inoubliables en toute sérénité."};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_onboarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv.setImageResource(images[position]);
        holder.title.setText(titles[position]);
        holder.desc.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() { return titles.length; }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv; TextView title, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivOnboarding);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDesc);
        }
    }
}