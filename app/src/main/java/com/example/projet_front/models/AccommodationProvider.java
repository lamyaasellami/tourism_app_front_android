package com.example.projet_front.models;

public class AccommodationProvider {
    private int id;
    private String name;
    private String type;
    private String description;
    private String websiteUrl;
    private String logoUrl;
    private boolean isFavorite;


    // ✅ REQUIRED for Retrofit/Gson
    public AccommodationProvider() {}
    public AccommodationProvider(int id, String name, String type, String description,
                                 String websiteUrl, String logoUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.logoUrl = logoUrl;
        this.isFavorite = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
