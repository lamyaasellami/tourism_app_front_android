package com.example.projet_front.models;

public class FavoriteProvider {
    private Long id;
    private Long userId;
    private String entityType; // Enum in backend, String in Android private Long entityId;
    private String createdAt; // ISO date string, can parse to Date if needed
    private PlaceResponse place;
    private AccommodationProvider accommodation;
    // Constructor
    public FavoriteProvider() {}
    public FavoriteProvider(Long id, Long userId, String entityType, String createdAt,
                            PlaceResponse place, AccommodationProvider accommodation) {
        this.id = id;
        this.userId = userId;
        this.entityType = entityType;
        this.createdAt = createdAt;
        this.place = place;
        this.accommodation = accommodation;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PlaceResponse getPlace() {
        return place;
    }

    public AccommodationProvider getAccommodation() {
        return accommodation;
    }


}
