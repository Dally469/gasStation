package com.example.gpsspstationdetector.models;

public class Location {
    private String id;
    private String name;
    private String phone;
    private boolean garageExist;
    private String description;
    private String latitude;
    private String longitude;

    public Location() {
    }

    public Location(String id, String name, String phone, boolean garageExist, String description, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.garageExist = garageExist;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGarageExist() {
        return garageExist;
    }

    public void setGarageExist(boolean garageExist) {
        this.garageExist = garageExist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
