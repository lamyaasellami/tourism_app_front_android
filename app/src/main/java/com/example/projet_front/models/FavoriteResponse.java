package com.example.projet_front.models;

public class FavoriteResponse {

    private Long favoriteId;
    private String entityType;
    private String createdAt;

    private PlaceResponse place;
    private AccommodationProvider accommodation;
    private TransportProvider transport;

    // getters & setters


    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
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

    public void setPlace(PlaceResponse place) {
        this.place = place;
    }

    public AccommodationProvider getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(AccommodationProvider accommodation) {
        this.accommodation = accommodation;
    }

    public TransportProvider getTransport() {
        return transport;
    }

    public void setTransport(TransportProvider transport) {
        this.transport = transport;
    }
}
