package com.example.first.homework09;

/**
 * Created by Chaithanya on 4/27/2017.
 */

public class TripPlaceClass {

    String place;
    String State;
    String Country;


    double latittude;
    double longitude;


    public TripPlaceClass() {
    }

    public TripPlaceClass(String place, double latittude, double longitude) {
        this.place = place;
        this.latittude = latittude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "TripPlaceClass{" +
                "place='" + place + '\'' +
                '}';
    }


    public double getLatittude() {
        return latittude;
    }

    public void setLatittude(double latittude) {
        this.latittude = latittude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public TripPlaceClass(String place) {
        this.place = place;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
