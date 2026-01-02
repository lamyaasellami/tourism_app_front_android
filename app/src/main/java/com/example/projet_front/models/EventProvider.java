package com.example.projet_front.models;

import java.time.LocalDate;
import java.util.Date;

public class EventProvider {

    private int eventId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String eventType;
    private Integer placeId;
    private String cityName;
    private String imgPath;
    private boolean isSpecial;
    private String websiteUrl;


    // ✅ REQUIRED for Retrofit/Gson
    public EventProvider() {}

    public EventProvider(int eventId, String name, String description, String startDate,
                         String endDate, String eventType, Integer placeId, String cityName,
                         String imgPath, boolean isSpecial, String websiteUrl) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventType = eventType;
        this.placeId = placeId;
        this.cityName = cityName;
        this.imgPath = imgPath;
        this.isSpecial = isSpecial;
        this.websiteUrl = websiteUrl;
    }

    // Getters and Setters

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
