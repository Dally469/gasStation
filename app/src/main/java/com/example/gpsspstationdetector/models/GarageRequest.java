package com.example.gpsspstationdetector.models;

public class GarageRequest {
    private String station_id;
    private String client_name;
    private String client_phone;
    private String client_plate_nbr;
    private double client_latitude;
    private double client_longitude;
    private String date;
    private String status;

    public GarageRequest() {
    }

    public GarageRequest(String station_id, String client_name, String client_phone, String client_plate_nbr, double client_latitude, double client_longitude, String date, String status) {
        this.station_id = station_id;
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.client_plate_nbr = client_plate_nbr;
        this.client_latitude = client_latitude;
        this.client_longitude = client_longitude;
        this.date = date;
        this.status = status;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getClient_plate_nbr() {
        return client_plate_nbr;
    }

    public void setClient_plate_nbr(String client_plate_nbr) {
        this.client_plate_nbr = client_plate_nbr;
    }

    public double getClient_latitude() {
        return client_latitude;
    }

    public void setClient_latitude(double client_latitude) {
        this.client_latitude = client_latitude;
    }

    public double getClient_longitude() {
        return client_longitude;
    }

    public void setClient_longitude(double client_longitude) {
        this.client_longitude = client_longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
