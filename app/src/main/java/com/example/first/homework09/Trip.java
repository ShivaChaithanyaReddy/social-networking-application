package com.example.first.homework09;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Chaithanya on 4/21/2017.
 */

public class Trip  implements Serializable{

    String name;
    String location;
    long timestamp;
    String admin;
    String tripID;

    String members;

    public Trip(String name, String location, long timestamp, String admin, String tripID, String members) {
        this.name = name;
        this.location = location;
        this.timestamp = timestamp;
        this.admin = admin;
        this.tripID = tripID;
        this.members = members;
    }

    public Trip() {
    }

    @Override
    public String toString() {
        return "Trip{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", timestamp=" + timestamp +
                ", admin='" + admin + '\'' +
                ", members='" + members + '\'' +
                '}';
    }


    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
