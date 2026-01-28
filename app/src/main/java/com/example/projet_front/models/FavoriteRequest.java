package com.example.projet_front.models;


public class FavoriteRequest {

    private long userId;
    private String entityType;
    private long entityId;

    public FavoriteRequest(long userId, String entityType, long entityId) {
        this.userId = userId;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
}
