package com.example.projet_front.models;


public class FavoriteRequest {

    private int userId;
    private int placeId;

    public FavoriteRequest(int userId, int placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPlaceId() {
        return placeId;
    }
}
